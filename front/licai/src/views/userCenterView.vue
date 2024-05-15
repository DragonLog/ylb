<template>
	<Header></Header>
	<div class="content clearfix">
	    <!--个人信息-->
	    <div class="user-head">
	        <div class="user-head-left fl">
	            <div class="user-head-img">
	                <img src="../assets/image/user-head.png" alt="">
	            </div>
	            <p>上传头像</p>
	        </div>
	        <div class="user-head-right fl">
	            <ul class="user-head-name fl">
	                <li><b>{{userBaseInfo.name}}</b></li>
	                <li>{{userBaseInfo.phone}}</li>
	                <li>最近登录：{{userBaseInfo.loginTime}}</li>
	            </ul>
	            <div class="user-head-money fr">
	                <p>可用余额：<span>￥{{userBaseInfo.money}}元</span></p>
	                <a href="javascript:void(0);" style="color: red;"  class="user-head-a1" @click="goLink('/page/user/userPay')">充值</a>
	                <a href="javascript:void(0);" style="color: red;"  class="user-head-a2" @click="goLink('/')">投资</a>
	            </div>
	        </div>
	
	    </div>
	    <!--记录-->
	    <div class="user-record-box clearfix">
	        <div class="user-record user-record-1">
	            <h3 class="user-record-title">最近投资</h3>
	            <table align="center" width="388" border="0" cellspacing="0" cellpadding="0">
	                <thead class="datail_thead">
	                    <tr>
	                        <th>序号</th>
	                        <th>投资产品</th>
	                        <th>投资金额</th>
	                        <th>投资时间</th>
	                    </tr>
	                </thead>
	                <tbody>
	                    <tr v-for="(item, index) in investList" :key="item.id">
	                        <td>{{index + 1}}</td>
	                        <td>{{item.productName}}</td>
	                        <td>{{item.bidMoney}}</td>
	                        <td>{{item.investTime}}</td>
	                    </tr>
	                </tbody>
	            </table>
	            <!--无记录-->
	            <p v-if="investList.length == 0" class="user-record-no">还没有投资记录，请投资：<a href="javascript:void(0);" @click="goLink('/')">投资</a></p>
	        </div>
	        <div class="user-record user-record-2">
	            <h3 class="user-record-title">最近充值</h3>
	            <table align="center" width="388" border="0" cellspacing="0" cellpadding="0">
	                <thead class="datail_thead">
	                    <tr>
	                        <th>序号</th>
	                        <th>充值结果</th>
	                        <th>充值金额</th>
	                        <th>充值时间</th>
	                    </tr>
	                </thead>
	                <tbody>
	                    <tr v-for="(item, index) in rechargeList" :key="item.id">
	                        <td>{{index + 1}}</td>
	                        <td>{{item.result}}</td>
	                        <td>{{item.rechargeMoney}}</td>
	                        <td>{{item.rechargeDate}}</td>
	                    </tr>
	                </tbody>
	            </table>
	            <!--无记录-->
	            <p v-if="rechargeList.length == 0" class="user-record-no">还没有充值记录，请充值：<a href="javascript:void(0);" @click="goLink('/page/user/userPay')">充值</a></p>
	        </div>
	        <div class="user-record user-record-3">
	            <h3 class="user-record-title ">最近收益</h3>
	            <table align="center" width="388" border="0" cellspacing="0" cellpadding="0">
	                <thead class="datail_thead">
	                    <tr>
	                        <th>序号</th>
	                        <th>项目名称</th>
	                        <th>收益日期</th>
	                        <th>收益金额</th>
	                    </tr>
	                </thead>
	                <tbody>
	                   <tr v-for="(item, index) in incomeList" :key="item.id">
	                       <td>{{index + 1}}</td>
	                       <td>{{item.productName}}</td>
	                       <td>{{item.incomeTime}}</td>
	                       <td>{{item.incomeMoney}}</td>
	                   </tr>
	                </tbody>
	            </table>
	            <!--无记录-->
	            <p v-if="incomeList.length == 0" class="user-record-no">还没有收益记录</p>
	        </div>
	
	    </div>
	
	
	</div>
	<Footer></Footer>
</template>

<script>
import header from "../components/common/header.vue"
import footer from "../components/common/footer.vue"
import {doGet} from "../api/httpRequest.js"
export default {
	name: "userCenterView",
	components:{
		"Header":header,
		"Footer":footer
	},
	mounted() {
		doGet("/v1/user/userCenter").then(resp => {
			if(resp && resp.data.code == 200) {
				this.userBaseInfo = resp.data.data;
			}
		});
		
		doGet("/v1/recharge/records", {pageNo:1, pageSize:6}).then(resp => {
			if(resp && resp.data.code == 200) {
				this.rechargeList = resp.data.list;
				console.log(resp.data.list);
			}
		});
		
		doGet("/v1/invest/records", {pageNo:1, pageSize:6}).then(resp => {
			if(resp && resp.data.code == 200) {
				this.investList = resp.data.list;
			}
		});
		
		doGet("/v1/income/records", {pageNo:1, pageSize:6}).then(resp => {
			if(resp && resp.data.code == 200) {
				this.incomeList = resp.data.list;
			}
		});
	},
	data() {
		return {
			userBaseInfo: {
				loginTime: "",
				money: 0,
				phone: "",
				name: "",
				headerUrl: ""
			},
			rechargeList:[
				{
				   id: 0,
				   result: "",
				   rechargeDate: "",
				   rechargeMoney: 0
				}
			],
			investList:[
				{
				  id: 0,
				  prodId: 0,
				  uid: 0,
				  bidMoney: 0,
				  bidTime: 0,
				  investTime: 0,
				  bidStatus: 0,
				  productName: ""
				}
			],
			incomeList:[
				{
				    id: 0,
				    uid: 0,
				    prodId: 0,
				    bidId: 0,
				    bidMoney: 0,
				    incomeDate: 0,
				    incomeMoney: 0,
				    incomeStatus: 0,
				    incomeTime: "",
				    productName: ""
				 }
			]
		}
	},
	methods:{
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
</style>
