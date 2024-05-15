<template>
	<Header></Header>
	<div class="login-content">
	    <div class="login-flex">
	        <div class="login-left"></div>
	        <!---->
	        <div class="login-box">
	            <h3 class="login-title">实名认证</h3>
	            <form action="" id="renZ_Submit">
	                <div class="alert-input">
	                    <input type="text" class="form-border user-name" v-model="name" @blur="checkName" name="username" placeholder="请输入您的真实姓名">
						<div class="err">{{nameErr}}</div>
	                    <p class="prompt_name"></p>
	                    <input type="text" class="form-border user-sfz" v-model="idCard" @blur="checkIdCard" name="sfz" placeholder="请输入15位或18位身份证号">
						<div class="err">{{idCardErr}}</div>
	                    <p class="prompt_sfz"></p>
	                    <input type="text" class="form-border user-num" v-bind:value="phone" name="mobile" placeholder="请输入11位手机号" readonly="true" style="color: #B6B8BA;">
	                    <p class="prompt_num"></p>
	                   <!-- <div class="form-yzm form-border">
	                        <input class="yzm-write" type="text" placeholder="输入短信验证码">
	                        <input class="yzm-send" type="text" value="获取验证码" disabled="disabled" id="yzmBtn" readonly="readonly" >
	                    </div>
	                    <p class="prompt_yan"></p> -->
	                </div>
	                <div class="alert-input-agree">
	                   <input type="checkbox" v-model="agree">&nbsp;我已阅读并同意<a href="javascript:;" target="_blank">《动力金融网注册服务协议》</a>
	                </div>
	                <div class="alert-input-btn">
	                    <input type="button" @click="requestRealName" class="login-submit" value="认证">
	                </div>
	            </form>
	            <div class="login-skip">
	                暂不认证？ <a href="javascript:void(0);" @click="goLink('/page/user/userCenter')">跳过</a>
	            </div>
	        </div>
	
	    </div>
	</div>
	<Footer></Footer>
</template>

<script>
import header from "../components/common/header.vue"
import footer from "../components/common/footer.vue"
import {doPost} from "../api/httpRequest.js"
import layx from "vue-layx"
export default {
	name: "realNameView",
	components:{
		"Header":header,
		"Footer":footer
	},
	data() {
		return {
			phone:"",
			name:"",
			nameErr:"",
			idCard:"",
			idCardErr:"",
			agree:false
		}
	},
	mounted() {
		let userInfo = window.localStorage.getItem("userInfo");
		if(userInfo) {
			this.phone = JSON.parse(userInfo).phone;
		}
	},
	methods: {
		checkName() {
			if (!this.name) {
				this.nameErr = "必须输入姓名";
			} else if (this.name.length < 2) {
				this.nameErr = "姓名格式不正确";
			} else if (!/^[\u4e00-\u9fa5]{0,}$/.test(this.name)) {
				this.nameErr = "姓名必须是中文";
			} else {
				this.nameErr = "";
			}
		},
		checkIdCard() {
			if (!this.idCard) {
				this.idCardErr = "必须输入身份证号";
			} else if (!/(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/.test(this.idCard)) {
				this.idCardErr = "身份证号格式不正确";
			} else {
				this.idCardErr = "";
			}
		},
		requestRealName() {
			this.checkName();
			this.checkIdCard();
			if (this.nameErr == "" && this.idCardErr == "") {
				if(this.agree) {
					let param = {
						name: this.name,
						idCard: this.idCard,
						phone: this.phone,
					//	忽略验证码
					};
					doPost('/v1/user/realName', param).then(resp=>{
						if (resp && resp.data.code == 200) {
							this.$router.push({
								path:"/page/user/userCenter"
							});
						} else {
							console.log("666");
							layx.msg(resp.data.msg, {dialogIcon:"warn", position:"ct"});
						}
					});
				} else {
					layx.msg("请阅读协议", {dialogIcon:"warn", position:"ct"});
				}
			}
		},
		goLink(url, params) {
			this.$router.push({
				path: url,
				query: params
			})
		}
	}
}
</script>

<style>
	.err{
		color: red;
		font-size: 15px;
	}
</style>
