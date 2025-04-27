<template>
  <view class="content">
    <view class="search-box">
      <input class="search-input" type="text" v-model="keyword" placeholder="请输入关键词" @confirm="search" confirm-type="search" />
      <text class="search-btn" @tap="search">搜索</text>
    </view>
    
    <!-- 搜索结果列表 -->
    <view v-if="productList.length > 0" class="product-list">
      <view v-for="(item, index) in productList" :key="index" class="product-item" @tap="navToDetailPage(item)">
        <view class="image-wrapper">
          <image :src="item.pic" mode="aspectFill"></image>
        </view>
        <view class="product-info">
          <text class="product-name">{{item.name}}</text>
          <text class="product-subtitle">{{item.subTitle}}</text>
          <text class="product-price">¥{{item.price}}</text>
        </view>
      </view>
    </view>
    
    <!-- 空结果提示 -->
    <view v-else-if="isSearched" class="empty-result">
      没有找到相关商品
    </view>
    
    <!-- 加载更多 -->
    <uni-load-more :status="loadingType"></uni-load-more>
  </view>
</template>

<script>
import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
import { searchProducts } from '@/api/product.js';

export default {
  components: {
    uniLoadMore
  },
  data() {
    return {
      keyword: '',
      productList: [],
      isSearched: false,
      loadingType: 'more',
      searchParam: {
        keyword: '',
        brandId: null,
        productCategoryId: null,
        pageNum: 1,
        pageSize: 10,
        sort: 0
      }
    };
  },
  onLoad(options) {
    // 如果有关键词参数，则自动搜索
    if (options.keyword) {
      this.keyword = options.keyword;
      this.search();
    }
  },
  // 下拉刷新
  onPullDownRefresh() {
    this.searchParam.pageNum = 1;
    this.productList = [];
    this.search('refresh');
  },
  // 上拉加载更多
  onReachBottom() {
    if (this.loadingType === 'nomore') return;
    this.searchParam.pageNum++;
    this.search('add');
  },
  methods: {
    // 搜索商品
    search(type = 'refresh') {
      if (!this.keyword) {
        this.$api.msg('请输入搜索关键词');
        return;
      }
      
      this.isSearched = true;
      this.searchParam.keyword = this.keyword;
      
      if (type === 'refresh') {
        this.searchParam.pageNum = 1;
        this.productList = [];
        this.loadingType = 'loading';
      }
      
      searchProducts(this.searchParam).then(response => {
        if (response.code === 200) {
          const data = response.data;
          const list = data.list || [];
          
          if (list.length === 0) {
            this.loadingType = 'nomore';
            if (type !== 'add') {
              this.productList = [];
            }
          } else {
            this.productList = type === 'add' ? [...this.productList, ...list] : list;
            
            if (list.length < this.searchParam.pageSize) {
              this.loadingType = 'nomore';
            } else {
              this.loadingType = 'more';
            }
          }
          
          if (type === 'refresh') {
            uni.stopPullDownRefresh();
          }
        } else {
          this.$api.msg(response.message || '搜索失败');
        }
      }).catch(err => {
        console.error(err);
        this.$api.msg('搜索失败，请稍后重试');
        this.loadingType = 'more';
        if (type === 'refresh') {
          uni.stopPullDownRefresh();
        }
      });
    },
    
    // 跳转到商品详情页
    navToDetailPage(item) {
      uni.navigateTo({
        url: `/pages/product/product?id=${item.id}`
      });
    }
  }
};
</script>

<style lang="scss">
  .content {
    padding-bottom: 30upx;
  }
  
  .search-box {
    display: flex;
    padding: 20upx 30upx;
    background: #fff;
    
    .search-input {
      flex: 1;
      height: 70upx;
      line-height: 70upx;
      padding: 0 20upx;
      font-size: 28upx;
      border-radius: 36upx;
      background: #f5f5f5;
    }
    
    .search-btn {
      width: 120upx;
      height: 70upx;
      line-height: 70upx;
      margin-left: 20upx;
      font-size: 28upx;
      color: #fff;
      background: #fa436a;
      border-radius: 36upx;
    }
  }
  
  .product-list {
    padding: 0 30upx;
    
    .product-item {
      display: flex;
      padding: 20upx 0;
      border-bottom: 1px solid #f5f5f5;
      
      .image-wrapper {
        width: 200upx;
        height: 200upx;
        border-radius: 6upx;
        overflow: hidden;
        
        image {
          width: 100%;
          height: 100%;
        }
      }
      
      .product-info {
        flex: 1;
        padding-left: 20upx;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        
        .product-name {
          font-size: 28upx;
          color: #333;
          line-height: 40upx;
          height: 80upx;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          -webkit-box-orient: vertical;
        }
        
        .product-subtitle {
          font-size: 24upx;
          color: #999;
          line-height: 32upx;
          height: 32upx;
          overflow: hidden;
          text-overflow: ellipsis;
          white-space: nowrap;
        }
        
        .product-price {
          font-size: 32upx;
          color: #fa436a;
          line-height: 1;
        }
      }
    }
  }
  
  .empty-result {
    padding: 100upx 0;
    text-align: center;
    font-size: 28upx;
    color: #999;
  }
</style>