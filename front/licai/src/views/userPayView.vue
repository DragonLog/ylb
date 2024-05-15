<template>
	<Header></Header>
	<!--充值-->
	<div class="user-pay-box clearfix">
	    <div class="user-pay-left fl">
	        <p class="user-pay-title1">我的账户</p>
	        <div class="user-pay-list">
	            <a href="javascript:;" class="user-pay-list-on">充值</a>
	            <a href="javascript:;" target="_blank">我的信息</a>
	            <a href="javascript:;" target="_blank">投资记录</a>
	            <a href="javascript:;" target="_blank">收益记录</a>
	        </div>
	    </div>
	    <div class="user-pay-right fl">
	        <p class="user-pay-title2">第三方支付平台</p>
	        <div class="user-pay-form">
	            <img src="../assets/image/pay-1.jpg" alt="">
	            <form action="http://localhost:9000/pay/kq/receive/recharge" target="_blank" id="money_submit">
	                <p class="user-pay-form-ts">请输入金额</p>
	                <input type="text" placeholder="元" value="0.01" name="rechargeMoney" class="number-money" >
	                <input type="hidden"name="uid" v-bind:value="userId">
	                <input type="submit" value="支付"  class="submit-btn">
	            </form>
	        </div>
	        <div class="user-pay-sm">
	            <h3>温馨提示</h3>
	            <p>1.为了您的使用安全，充值操作建议不要使用公共电脑。</p>
	            <p>2.银行卡充值限额，由各银行限制。</p>
	            <p>3.品台禁止用卡套现、虚拟交易、洗钱等行为，一经发现并确认，将终止该账户的使用。</p>
	            <p>4.如果充值中出现问题，请联系客服400-890-0000。</p>
	        </div>
	    </div>
	
	</div>
	<Footer></Footer>
</template>

<script>
import header from "../components/common/header.vue"
import footer from "../components/common/footer.vue"
export default {
	name: "userPayView",
	components:{
		"Header":header,
		"Footer":footer
	},
	data() {
		return {
			userId:0
		}
	},
	mounted() {
		let userInfo = window.localStorage.getItem("userInfo");
		if (userInfo) {
			this.userId = JSON.parse(userInfo).uid;
		}
	}
}
</script>

<style>
</style>
