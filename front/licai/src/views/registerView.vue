<template>
	<Header></Header>
	
	<div class="login-content">
	    <div class="login-flex">
	        <div class="login-left">
	            <p>万民用户知心托付&nbsp;&nbsp;&nbsp;&nbsp;<span>{{historyAvgRate}}%</span>历史年化收益</p>
	            <p>千万级技术研发投入&nbsp;&nbsp;&nbsp;&nbsp;亿级注册资本平台  </p>
	        </div>
	        <!---->
	        <div class="login-box">
	            <h3 class="login-title">用户注册</h3>
	            <form action="" id="register_Submit">
	                <div class="alert-input">
	                    <input type="text" class="form-border user-num" @blur="checkPhone" v-model="phone" name="mobile" placeholder="请输入11位手机号">
						<div class="err">{{phoneErr}}</div>
	                    <p class="prompt_num"></p>
	                    <input type="password" placeholder="请输入6-20位英文和数字混合密码" v-model="password" @blur="checkPassword" class="form-border user-pass" autocomplete name="password">
						<div class="err">{{passwordErr}}</div>
	                    <p class="prompt_pass"></p>
	                    <div class="form-yzm form-border">
	                        <input class="yzm-write" type="text" name="" v-model="code" @blur="checkCode" placeholder="输入短信验证码">
	                        <input class="yzm-send" type="button" v-bind:value="codeText" id="yzmBtn" @click="requestSmsCode">
	                    </div>
						<div class="err">{{codeErr}}</div>
	                    <p class="prompt_yan"></p>
	                </div>
	                <div class="alert-input-agree">
	                    <input type="checkbox" v-model="agree">&nbsp;我已阅读并同意<a href="javascript:;" target="_blank">《动力金融网注册服务协议》</a>
	                </div>
	                <div class="alert-input-btn">
	                    <input type="button" class="login-submit" value="注册" @click="requestUserRegister">
	                </div>
	            </form>
	            <div class="login-skip">
	                已有账号？ <a href="javascript:void(0)" @click="goLink('/page/user/login')">登录</a>
	            </div>
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
	name:"registerView",
	components:{
		"Header":header,
		"Footer":footer
	},
	mounted() {
		doGet("/v1/plat/info", {}).then(resp=>{
			if(resp) {
				this.historyAvgRate = resp.data.data.historyAvgRate;
			}
		})
	},
	data() {
		return {
			historyAvgRate:0,
			phone:"",
			phoneErr:"",
			password:"",
			passwordErr:"",
			code:"",
			codeErr:"",
			codeText:"获取验证码",
			agree:false
		}
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
				doGet("/v1/user/phone/exists", {phone:this.phone}).then(resp=>{
					if(resp && resp.data.code == 200) {
						
					} else {
						this.phoneErr = resp.data.msg;
					}
				});
			}
		},
		checkPassword() {
			if (this.password == "" || this.password == undefined) {
				this.passwordErr = "请输入密码";
			} else if (this.password.length < 6 || this.password.length > 20) {
				this.passwordErr = "密码长度是6-20位"
			} else if (!/^[0-9a-zA-Z]+$/.test(this.password)) {
				this.passwordErr = "密码只能使用数字和字母";
			} else if (!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(this.password)) {
				this.passwordErr = "密码是数字和字母的混合";
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
				//setTimeout(()=>{
				//	clearInterval(intervalId);
				//}, 182 * 1000);
				doGet("/v1/sms/code/register", {phone:this.phone}).then(resp=>{
					if (resp && (resp.data.code == 200 || resp.data.code == 1006)) {
						layx.msg("短信已经发送了", {dialogIcon:"success", position:"ct"});
					} else {
						layx.msg(resp.data.msg, {dialogIcon:"warn", position:"ct"});
					}
				});
			}
		},
		requestUserRegister() {
			this.checkPhone();
			this.checkPassword();
			this.checkCode();
			if (this.phoneErr == "" && this.passwordErr == "" && this.codeErr == "") {
				if (this.agree) {
					let newPassword = md5(this.password);
					doPost1("/v1/user/register", {
						phone:this.phone,
						miMa:newPassword,
						yanZhengMa:this.code
					}).then(resp=>{
						if (resp && resp.data.code == 200) {
							this.$router.push({
								path:"/page/user/login"
							})
						}
					});
				} else {
					layx.msg("请阅读注册协议", {dialogIcon:"warn", position:"ct"});
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
