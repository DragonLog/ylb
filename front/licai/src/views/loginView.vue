<template>
	<Header></Header>
	<div class="login-content">
	    <div class="login-flex">
	        <div class="login-left">
	            <h3>加入动力金融网</h3>
	            <p>坐享<span>{{platInfo.historyAvgRate}}%</span>历史年化收益</p>
	            <p>平台用户<span>{{platInfo.registerUsers}}</span>位  </p>
	            <p>累计成交金额<span>{{platInfo.sumBidMoney}}</span>元</p>
	        </div>
	        <!---->
	        <div class="login-box">
	            <h3 class="login-title">欢迎登录</h3>
	            <form action="" id="login_Submit">
	                <div class="alert-input">
	                    <!--<input class="form-border user-name" name="username" type="text" placeholder="您的姓名">
	                    <p class="prompt_name"></p>-->
	                    <input type="text" class="form-border user-num" v-model="phone" @blur="checkPhone" name="mobile" placeholder="请输入11位手机号">
						<div class="err">{{phoneErr}}</div>
	                    <p class="prompt_num"></p>
	                    <input type="password" placeholder="请输入登录密码" v-model="password" @blur="checkPassword" class="form-border user-pass" autocomplete name="password">
						<div class="err">{{passwordErr}}</div>
	                    <p class="prompt_pass"></p>
	                    <div class="form-yzm form-border">
	                        <input class="yzm-write" v-model="code" @blur="checkCode" type="text" placeholder="输入短信验证码">
	                        <input class="yzm-send" type="button" v-bind:value="codeText" @click="requestSmsCode" id="yzmBtn">
	                    </div>
						<div class="err">{{codeErr}}</div>
	                    <p class="prompt_yan"></p>
	                </div>
	                <div class="alert-input-btn">
	                    <input type="button" @click="userLogin" class="login-submit" value="登录">
	                </div>
	            </form>
	
	        </div>
	
	    </div>
	</div>
	<Footer></Footer>
</template>

<script>
import header from "../components/common/header.vue"
import footer from "../components/common/footer.vue"
import {doGet} from "../api/httpRequest.js"
import {doPost1} from "../api/httpRequest.js"
import md5 from "js-md5"
import layx from "vue-layx"
export default {
	name: "loginView",
	components:{
		"Header":header,
		"Footer":footer
	},
	data() {
		return {
			platInfo:{
				"historyAvgRate": 0.00,
				"sumBidMoney": 0.00,
				"registerUsers": 0
			},
			phone:"",
			phoneErr:"",
			password:"",
			passwordErr:"",
			code:"",
			codeErr:"",
			codeText:"获取验证码"
		}
	},
	mounted() {
		doGet("/v1/plat/info", {}).then(resp=>{
			if(resp) {
				this.platInfo = resp.data.data
			}
		})
	},
	methods:{
		checkPhone() {
			if (this.phone== "" || this.phone == undefined) {
				this.phoneErr = "请输入手机号";
			} else if (this.phone.length != 11) {
				this.phoneErr = "手机号长度错误";
			} else if (!/^1[1-9]\d{9}$/.test(this.phone)) {
				this.phoneErr = "手机号格式不正确";
			} else {
				this.phoneErr = "";
			}
		},
		checkPassword() {
			if (this.password == "" || this.password == undefined) {
				this.passwordErr = "请输入密码";
			} else {
				this.passwordErr = "";
			}
		},
		checkCode() {
			if (this.code == "" || this.code == undefined) {
				this.codeErr = "必须输入验证码";
			} else if (this.code.length != 4) {
				this.codeErr = "验证码是4位";
			} else {
				this.codeErr = "";
			}
		},
		requestSmsCode() {
			this.checkPhone();
			if (this.phoneErr == "") {
				let btn = document.getElementById("yzmBtn");
				btn.style.color = "red";
				btn.disabled = true;
				let second = 60;
				const intervalId = setInterval(()=>{
					if (second < 0) {
						this.codeText = "获取验证码";
						btn.style.color = "#688EF9";
						btn.disabled = false;
						clearInterval(intervalId);
					} else {
						this.codeText = second + "秒后重新获取";
						second = second - 1;
					}
				}, 1000);
				doGet("/v1/sms/code/login", {phone:this.phone}).then(resp=>{
					if (resp && (resp.data.code == 200 || resp.data.code == 1006)) {
						layx.msg("短信已经发送了", {dialogIcon:"success", position:"ct"});
					} else {
						layx.msg(resp.data.msg, {dialogIcon:"warn", position:"ct"});
					}
				});
			}
		},
		userLogin() {
			this.checkPhone();
			this.checkPassword();
			this.checkCode();
			if (this.phoneErr == "" && this.passwordErr == "" && this.codeErr == "") {
				let param = {
					phone: this.phone,
					miMa: md5(this.password),
					yanZhengMa: this.code
				};
				doPost1("/v1/user/login", param).then(resp=>{
					if (resp && resp.data.code == 200) {
						window.localStorage.setItem("token", resp.data.accessToken);
						window.localStorage.setItem("userInfo", JSON.stringify(resp.data.data));
						
						if (resp.data.data.name == "") {
							this.$router.push({
								path:"/page/user/realName"
							});
						} else {
							this.$router.push({
								path:"/page/user/userCenter"
							});
						}
					} else {
						layx.msg(resp.data.msg, {dialogIcon:"warn", position:"ct"});
					}
				});
			}
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
