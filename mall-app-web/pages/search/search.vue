<template>
	<view class="content">
		<!-- 搜索框 -->
		<view class="search-box">
			<view class="search-form">
				<input class="search-input" type="text" v-model="keyword" placeholder="请输入商品关键词" @confirm="search" confirm-type="search"/>
				<button class="search-btn" @click="search">搜索</button>
			</view>
		</view>
		
		<!-- 空白页 -->
		<empty v-if="productList.length === 0 && !loading && isSearched"></empty>
		
		<!-- 产品列表 -->
		<view class="goods-list" v-if="productList.length > 0">
			<view v-for="(item, index) in productList" :key="index" class="goods-item" @click="navToDetailPage(item)">
				<view class="image-wrapper">
					<image :src="item.pic" mode="aspectFill"></image>
				</view>
				<text class="title clamp">{{item.name}}</text>
				<text class="title2">{{item.subTitle}}</text>
				<view class="price-box">
					<text class="price">{{item.price}}</text>
				</view>
			</view>
		</view>
		
		<!-- 加载更多 -->
		<uni-load-more :status="loadingType"></uni-load-more>
	</view>
</template>

<script>
	import empty from "@/components/empty";
	import uniLoadMore from '@/components/uni-load-more/uni-load-more.vue';
	import { searchProducts } from '@/api/esProduct.js';
	
	export default {
		components: {
			empty,
			uniLoadMore
		},
		data() {
			return {
				keyword: '',
				loading: false,
				loadingType: 'more',
				productList: [],
				pageNum: 1,
				pageSize: 10,
				hasMore: true,
				isSearched: false // 添加标记，表示是否已经搜索过
			}
		},
		onLoad(options) {
			// 如果有传入的关键词参数，则进行搜索
			if (options.keyword) {
				this.keyword = options.keyword;
				this.search();
			}
		},
		methods: {
			// 搜索方法
			search() {
				if (!this.keyword.trim()) {
					uni.showToast({
						title: '请输入搜索关键词',
						icon: 'none'
					});
					return;
				}
				
				// 重置搜索状态
				this.pageNum = 1;
				this.productList = [];
				this.hasMore = true;
				this.isSearched = true; // 标记已经搜索过
				this.loadingType = 'more';
				
				// 执行搜索
				this.loadData();
				
				// 记录搜索历史（可选功能）
				this.saveSearchHistory(this.keyword);
			},
			
			// 保存搜索历史
			saveSearchHistory(keyword) {
				try {
					// 获取现有搜索历史
					let history = uni.getStorageSync('searchHistory') || [];
					// 如果不是数组，初始化为空数组
					if (!Array.isArray(history)) {
						history = [];
					}
					// 移除已存在的相同关键词
					history = history.filter(item => item !== keyword);
					// 将新关键词添加到开头
					history.unshift(keyword);
					// 限制历史记录数量
					if (history.length > 10) {
						history = history.slice(0, 10);
					}
					// 保存到本地存储
					uni.setStorageSync('searchHistory', history);
				} catch (e) {
					console.error('保存搜索历史失败', e);
				}
			},
			
			// 加载数据
			loadData() {
				if (!this.hasMore) return;
				
				this.loading = true;
				this.loadingType = 'loading';
				
				const params = {
					keyword: this.keyword,
					pageNum: this.pageNum - 1,
					pageSize: this.pageSize
				};
				
				searchProducts(params).then(response => {
					console.log('搜索结果:', response);
					const { data } = response;
					if (data && data.list) {
						if (data.list.length > 0) {
							this.productList = [...this.productList, ...data.list];
							this.hasMore = this.pageNum * this.pageSize < data.total;
							this.loadingType = this.hasMore ? 'more' : 'noMore';
							this.pageNum++;
						} else {
							this.loadingType = 'noMore';
							this.hasMore = false;
						}
					}
					this.loading = false;
				}).catch((error) => {
					console.error('搜索失败:', error);
					this.loading = false;
					this.loadingType = 'more';
					uni.showToast({
						title: '搜索失败，请重试',
						icon: 'none'
					});
				});
			},
			
			// 跳转到商品详情页
			navToDetailPage(item) {
				uni.navigateTo({
					url: `/pages/product/product?id=${item.id}`
				});
			}
		},
		// 下拉刷新
		onPullDownRefresh() {
			if (this.keyword) {
				this.pageNum = 1;
				this.productList = [];
				this.hasMore = true;
				this.loadData();
			}
			uni.stopPullDownRefresh();
		},
		// 上拉加载更多
		onReachBottom() {
			if (this.hasMore) {
				this.loadData();
			}
		}
	}
</script>

<style lang="scss">
	.content {
		padding-bottom: 30upx;
	}
	
	.search-box {
		width: 100%;
		background-color: #fff;
		padding: 20upx 30upx;
		box-sizing: border-box;
		position: sticky;
		top: 0;
		z-index: 10;
		box-shadow: 0 2upx 10upx rgba(0,0,0,0.1);
		
		.search-form {
			display: flex;
			align-items: center;
			
			.search-input {
				flex: 1;
				height: 70upx;
				background: #f5f5f5;
				border-radius: 35upx;
				padding: 0 30upx;
				font-size: 28upx;
			}
			
			.search-btn {
				width: 120upx;
				height: 70upx;
				line-height: 70upx;
				margin-left: 20upx;
				background: $uni-color-primary;
				color: #fff;
				border-radius: 35upx;
				font-size: 28upx;
				padding: 0;
			}
		}
	}
	
	/* 商品列表 */
	.goods-list {
		display: flex;
		flex-wrap: wrap;
		padding: 0 30upx;
		background: #fff;

		.goods-item {
			display: flex;
			flex-direction: column;
			width: 48%;
			padding-bottom: 40upx;

			&:nth-child(2n+1) {
				margin-right: 4%;
			}
		}

		.image-wrapper {
			width: 100%;
			height: 330upx;
			border-radius: 3px;
			overflow: hidden;

			image {
				width: 100%;
				height: 100%;
				opacity: 1;
			}
		}

		.title {
			font-size: $font-lg;
			color: $font-color-dark;
			line-height: 80upx;
		}

		.title2 {
			font-size: $font-sm;
			color: $font-color-light;
			line-height: 40upx;
			height: 80upx;
			overflow: hidden;
			text-overflow: ellipsis;
			display: block;
		}

		.price-box {
			display: flex;
			align-items: center;
			justify-content: space-between;
			padding-right: 10upx;
			font-size: 24upx;
			color: $font-color-light;
		}

		.price {
			font-size: $font-lg;
			color: $uni-color-primary;
			line-height: 1;

			&:before {
				content: '￥';
				font-size: 26upx;
			}
		}
	}
	
	.clamp {
		overflow: hidden;
		text-overflow: ellipsis;
		display: -webkit-box;
		-webkit-line-clamp: 2;
		-webkit-box-orient: vertical;
	}
</style>