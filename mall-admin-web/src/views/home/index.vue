<template>
  <div class="app-container">
    <div class="total-layout">
      <el-row :gutter="20">
        <el-col :span="10">
          <div class="total-frame" style="text-align: center;">
            <img :src="img_home_order" class="total-icon">
            <div class="total-title">订单总数</div>
            <div class="total-value">{{OrderCount}}</div>
          </div>
        </el-col>
        <el-col :span="10">
          <div class="total-frame"  style="text-align: center;">
            <img :src="img_home_today_amount" class="total-icon">
            <div class="total-title">销售总额</div>
            <div class="total-value">￥{{salesAmount}}</div>
          </div>
        </el-col>
        <!-- <el-col :span="6">
          <div class="total-frame">
            <img :src="img_home_yesterday_amount" class="total-icon">
            <div class="total-title">昨日销售总额</div>
            <div class="total-value">￥11203.00</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="total-frame">
            <svg-icon icon-class="total-week" class="total-icon">
            </svg-icon>
            <div class="total-title">近7天销售总额</div>
            <div class="total-value">￥58867.00</div>
          </div>
        </el-col> -->
      </el-row>
    </div>
    <div class="un-handle-layout">
      <div class="layout-title">待处理事务</div>
      <div class="un-handle-content">
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">待付款订单</span>
              <span style="float: right" class="color-danger">(1)</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">已完成订单</span>
              <span style="float: right" class="color-danger">(12)</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">待确认收货订单</span>
              <span style="float: right" class="color-danger">(5)</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">待发货订单</span>
              <span style="float: right" class="color-danger">(3)</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">新缺货登记</span>
              <span style="float: right" class="color-danger">(2)</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">待处理退款申请</span>
              <span style="float: right" class="color-danger">(2)</span>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">已发货订单</span>
              <span style="float: right" class="color-danger">(6)</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="un-handle-item">
              <span class="font-medium">待处理退货订单</span>
              <span style="float: right" class="color-danger">(3)</span>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    <div class="overview-layout">
      <el-row :gutter="20">
        <el-col :span="12">
          <div class="out-border">
            <div class="layout-title">商品总览</div>
            <div style="padding: 40px">
              <el-row>
                <el-col :span="6" class="color-danger overview-item-value">15</el-col>
                <el-col :span="6" class="color-danger overview-item-value">43</el-col>
                <el-col :span="6" class="color-danger overview-item-value">10</el-col>
                <el-col :span="6" class="color-danger overview-item-value">79</el-col>
              </el-row>
              <el-row class="font-medium">
                <el-col :span="6" class="overview-item-title">已下架</el-col>
                <el-col :span="6" class="overview-item-title">已上架</el-col>
                <el-col :span="6" class="overview-item-title">库存紧张</el-col>
                <el-col :span="6" class="overview-item-title">全部商品</el-col>
              </el-row>
            </div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="out-border">
            <div class="layout-title">用户总览</div>
            <div style="padding: 40px">
              <el-row>
                <el-col :span="6" class="color-danger overview-item-value">3</el-col>
                <el-col :span="6" class="color-danger overview-item-value">6</el-col>
                <el-col :span="6" class="color-danger overview-item-value">15</el-col>
                <el-col :span="6" class="color-danger overview-item-value">12</el-col>
              </el-row>
              <el-row class="font-medium">
                <el-col :span="6" class="overview-item-title">今日新增</el-col>
                <el-col :span="6" class="overview-item-title">昨日新增</el-col>
                <el-col :span="6" class="overview-item-title">本月新增</el-col>
                <el-col :span="6" class="overview-item-title">会员总数</el-col>
              </el-row>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    
  </div>
</template>

<script>
  import {str2Date} from '@/utils/date';
  import img_home_order from '@/assets/images/home_order.png';
  import img_home_today_amount from '@/assets/images/home_today_amount.png';
  import img_home_yesterday_amount from '@/assets/images/home_yesterday_amount.png';
  import {getOrderCount} from '@/api/order'; // 导入获取今日订单总数的API
  import {fetchSalesAmount} from '@/api/order'; // 导入获取今日销售总额的API
  const DATA_FROM_BACKEND = {
    columns: ['date', 'orderCount','orderAmount'],
    rows: [
      {date: '2025-3-21', orderCount: 1, orderAmount: 1093},
      {date: '2025-3-22', orderCount: 3, orderAmount: 12241},
      {date: '2025-3-23', orderCount: 1, orderAmount: 3623},
      {date: '2025-3-25', orderCount: 2, orderAmount: 6423},
      {date: '2025-3-26', orderCount: 3, orderAmount: 9492},
      {date: '2025-3-28', orderCount: 1, orderAmount: 5293},
      {date: '2025-3-29', orderCount: 5, orderAmount: 21293},
      {date: '2025-3-31', orderCount: 1, orderAmount: 6293},
      {date: '2025-4-2', orderCount: 4, orderAmount: 15293},
      {date: '2025-4-6', orderCount: 2, orderAmount: 5293},
      {date: '2025-4-7', orderCount: 3, orderAmount: 9293},
      {date: '2025-4-9', orderCount: 6, orderAmount: 28293},
      {date: '2025-4-10', orderCount: 1, orderAmount: 7293},
      {date: '2025-4-11', orderCount: 5, orderAmount: 18293},
      {date: '2025-4-13', orderCount: 2, orderAmount: 4293}
    ]
  };
  export default {
    name: 'home',
    data() {
      return {
        pickerOptions: {
          shortcuts: [{
            text: '最近一周',
            onClick(picker) {
              let start = new Date(2025,2,21);
              const end = new Date(start.getTime() + 1000 * 60 * 60 * 24 * 7);
              picker.$emit('pick', [start, end]);
            }
          }, {
            text: '最近一月',
            onClick(picker) {
              let start = new Date(2025,2,21);
              const end = new Date(start.getTime() + 1000 * 60 * 60 * 24 * 30);
              picker.$emit('pick', [start, end]);
            }
          }]
        },
        orderCountDate: '',
        chartSettings: {
          xAxisType: 'time',
          area:true,
          axisSite: { right: ['orderAmount']},
        labelMap: {'orderCount': '订单数量', 'orderAmount': '订单金额'}},
        chartData: {
          columns: [],
          rows: []
        },
        loading: false,
        dataEmpty: false,
        img_home_order,
        img_home_today_amount,
        img_home_yesterday_amount,
        OrderCount: 0, // 添加今日订单总数属性
        salesAmount: 0
      }
    },
    created(){
      this.initOrderCountDate();
      this.getData();
      this.getOrderCount(); // 获取今日订单总数
      this.getSalesAmount();
    },
    methods:{
      handleDateChange(){
        this.getData();
      },
      initOrderCountDate(){
        let start = new Date(2025,3,21);  
        const end = new Date(start.getTime() + 1000 * 60 * 60 * 24 * 7);
        this.orderCountDate=[start,end];
      },
      getData(){
        setTimeout(() => {
          this.chartData = {
            columns: ['date', 'orderCount','orderAmount'],
            rows: []
          };
          for(let i=0;i<DATA_FROM_BACKEND.rows.length;i++){
            let item=DATA_FROM_BACKEND.rows[i];
            let currDate=str2Date(item.date);
            let start=this.orderCountDate[0];
            let end=this.orderCountDate[1];
            if(currDate.getTime()>=start.getTime()&&currDate.getTime()<=end.getTime()){
              this.chartData.rows.push(item);
            }
          }
          this.dataEmpty = false;
          this.loading = false
        }, 1000)
      },
      getOrderCount() {
        getOrderCount().then(response => {
          this.OrderCount = response.data;
        });
      },
      getSalesAmount() {
        fetchSalesAmount().then(response => {
          this.salesAmount = response.data;
        });
      }
    }
  }
</script>

<style scoped>
  .app-container {
    margin-top: 40px;
    margin-left: 120px;
    margin-right: 120px;
  }

  .address-layout {
  }

  .total-layout {
    margin-top: 20px;
  }

  .total-frame {
    border: 1px solid #DCDFE6;
    padding: 20px;
    height: 100px;
  }

  .total-icon {
    color: #409EFF;
    width: 60px;
    height: 60px;
  }

  .total-title {
    position: relative;
    font-size: 16px;
    color: #909399;
    left: 70px;
    top: -50px;
  }

  .total-value {
    position: relative;
    font-size: 18px;
    color: #606266;
    left: 70px;
    top: -40px;
  }

  .un-handle-layout {
    margin-top: 20px;
    border: 1px solid #DCDFE6;
  }

  .layout-title {
    color: #606266;
    padding: 15px 20px;
    background: #F2F6FC;
    font-weight: bold;
  }

  .un-handle-content {
    padding: 20px 40px;
  }

  .un-handle-item {
    border-bottom: 1px solid #EBEEF5;
    padding: 10px;
  }

  .overview-layout {
    margin-top: 20px;
  }

  .overview-item-value {
    font-size: 24px;
    text-align: center;
  }

  .overview-item-title {
    margin-top: 10px;
    text-align: center;
  }

  .out-border {
    border: 1px solid #DCDFE6;
  }

  .statistics-layout {
    margin-top: 20px;
    border: 1px solid #DCDFE6;
  }
  .mine-layout {
    position: absolute;
    right: 140px;
    top: 107px;
    width: 250px;
    height: 235px;
  }
  .address-content{
    padding: 20px;
    font-size: 18px
  }
</style>
