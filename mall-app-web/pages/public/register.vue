<template>
	<view class="container">
		<view class="left-bottom-sign"></view>
		<view class="back-btn yticon icon-zuojiantou-up" @click="navBack"></view>
		<view class="right-top-sign"></view>
		<!-- 设置白色背景防止软键盘把下部绝对定位元素顶上来盖住输入框等 -->
		<view class="wrapper">
			<view class="left-top-sign">REGISTER</view>
			<view class="welcome">
				欢迎注册
			</view>
			<view class="input-content">
				<view class="input-item">
					<text class="tit">用户名</text>
					<input 
						type="text" 
						:value="username" 
						placeholder="请输入用户名"
						maxlength="20"
						@input="usernameInput"
					/>
				</view>
				<view class="input-item">
					<text class="tit">密码</text>
					<input 
						type="password" 
						:value="password" 
						placeholder="请输入密码"
						maxlength="20"
						@input="passwordInput"
					/>
				</view>
				<view class="input-item">
					<text class="tit">确认密码</text>
					<input 
						type="password" 
						:value="confirmPassword" 
						placeholder="请再次输入密码"
						maxlength="20"
						@input="confirmPasswordInput"
					/>
				</view>
				<view class="input-item">
					<text class="tit">手机号码</text>
					<input 
						type="number" 
						:value="mobile" 
						placeholder="请输入手机号码"
						maxlength="11"
						@input="mobileInput"
					/>
				</view>
			</view>
			<button class="confirm-btn" @click="toRegister" :disabled="logining">注册</button>
			<view class="register-section">
				已有账号?
				<text @click="toLogin">马上登录</text>
			</view>
			<view class="oauth-row" v-if="hasProvider" id="oauth-row">
				<view class="oauth-image" v-for="provider in providerList" :key="provider.value">
					<image :src="provider.image" @tap="oauth(provider.value)"></image>
				</view>
			</view>
		</view>
		
	</view>
</template>

<script>
	import {
		mapState,
		mapMutations
	} from 'vuex'
	
	export default {
		data() {
			return {
				username: '',
				password: '',
				confirmPassword: '',
				mobile: '',
				logining: false,
				hasProvider: false,
				providerList: []
			}
		},
		computed: {
			...mapState(['hasLogin', 'userInfo'])
		},
		onLoad() {
			// 获取第三方登录服务商信息
			uni.getProvider({
				service: 'oauth',
				success: (res) => {
					if (res.provider && res.provider.length) {
						this.hasProvider = true;
						for (let i = 0; i < res.provider.length; i++) {
							switch (res.provider[i]) {
								case 'weixin':
									this.providerList.push({
										value: res.provider[i],
										image: '/static/app-plus/weixin.png'
									});
									break;
								case 'qq':
									this.providerList.push({
										value: res.provider[i],
										image: '/static/app-plus/qq.png'
									});
									break;
								case 'sinaweibo':
									this.providerList.push({
										value: res.provider[i],
										image: '/static/app-plus/weibo.png'
									});
									break;
							}
						}
					}
				}
			});
		},
		methods: {
			...mapMutations(['login']),
			usernameInput(e) {
				this.username = e.detail.value;
			},
			passwordInput(e) {
				this.password = e.detail.value;
			},
			confirmPasswordInput(e) {
				this.confirmPassword = e.detail.value;
			},
			mobileInput(e) {
				this.mobile = e.detail.value;
			},
			navBack() {
				uni.navigateBack();
			},
			toLogin() {
				uni.navigateTo({
					url: '/pages/public/login'
				});
			},
			toRegister() {
				if (this.logining) {
					return;
				}
				
				if (this.username.length < 3) {
					uni.showToast({
						icon: 'none',
						title: '用户名不能少于3个字符'
					});
					return;
				}
				if (this.password.length < 6) {
					uni.showToast({
						icon: 'none',
						title: '密码不能少于6个字符'
					});
					return;
				}
				if (this.password !== this.confirmPassword) {
					uni.showToast({
						icon: 'none',
						title: '两次输入的密码不一致'
					});
					return;
				}
				if (!/^1\d{10}$/.test(this.mobile)) {
					uni.showToast({
						icon: 'none',
						title: '手机号格式不正确'
					});
					return;
				}
				
				this.logining = true;
				
				// 模拟注册
				setTimeout(() => {
					this.logining = false;
					uni.showToast({
						title: '注册成功'
					});
					// 注册成功后跳转到登录页
					uni.navigateTo({
						url: '/pages/public/login'
					});
				}, 1500);
				
				// 实际开发中，这里应该调用注册接口
				// this.$api.register({
				//     username: this.username,
				//     password: this.password,
				//     mobile: this.mobile
				// }).then(res => {
				//     this.logining = false;
				//     if(res.code === 200){
				//         uni.showToast({
				//             title: '注册成功'
				//         });
				//         uni.navigateTo({
				//             url: '/pages/public/login'
				//         });
				//     }else{
				//         uni.showToast({
				//             icon: 'none',
				//             title: res.msg
				//         });
				//     }
				// }).catch(() => {
				//     this.logining = false;
				// });
			},
			oauth(value) {
				uni.login({
					provider: value,
					success: (res) => {
						uni.getUserInfo({
							provider: value,
							success: (infoRes) => {
								// 实际开发中，这里应该调用服务端接口，将数据发送到服务端进行第三方登录
								console.log('用户信息：' + JSON.stringify(infoRes.userInfo));
								uni.showToast({
									title: '登录成功'
								});
								this.login(infoRes.userInfo);
								uni.switchTab({
									url: '/pages/index/index'
								});
							}
						});
					},
					fail: (err) => {
						console.error('授权登录失败：' + JSON.stringify(err));
					}
				});
			}
		},
	}
</script>

<style lang='scss'>
	page {
		background: #fff;
	}
	
	.empty {
		position: relative;
		width: 100%;
		padding: 20upx;
		display: flex;
		justify-content: center;
		flex-direction: column;
		align-items: center;
		background: #fff;
	
		image {
			width: 240upx;
			height: 240upx;
			margin-bottom: 30upx;
		}
		.empty-tips {
			display: flex;
			font-size: $font-sm+10upx;
			color: $font-color-disabled;
		
			.navigator {
				color: $uni-color-primary;
				margin-left: 0upx;
			}
		}
	}

	.container {
		padding-top: 115px;
		position: relative;
		width: 100vw;
		height: 100vh;
		overflow: hidden;
		background: #fff;
	}

	.wrapper {
		position: relative;
		z-index: 90;
		background: #fff;
		padding-bottom: 40upx;
	}
	
	.register-code {
		margin-top: 40upx;
	}

	.back-btn {
		position: absolute;
		left: 40upx;
		z-index: 9999;
		padding-top: var(--status-bar-height);
		top: 40upx;
		font-size: 40upx;
		color: $font-color-dark;
	}

	.left-top-sign {
		font-size: 120upx;
		color: $page-color-base;
		position: relative;
		left: -16upx;
	}

	.right-top-sign {
		position: absolute;
		top: 80upx;
		right: -30upx;
		z-index: 95;

		&:before,
		&:after {
			display: block;
			content: "";
			width: 400upx;
			height: 80upx;
			background: #b4f3e2;
		}

		&:before {
			transform: rotate(50deg);
			border-radius: 0 50px 0 0;
		}

		&:after {
			position: absolute;
			right: -198upx;
			top: 0;
			transform: rotate(-50deg);
			border-radius: 50px 0 0 0;
			/* background: pink; */
		}
	}

	.left-bottom-sign {
		position: absolute;
		left: -270upx;
		bottom: -320upx;
		border: 100upx solid #d0d1fd;
		border-radius: 50%;
		padding: 180upx;
	}
	
	.welcome {
		position: relative;
		left: 50upx;
		top: -90upx;
		font-size: 46upx;
		color: #555;
		text-shadow: 1px 0px 1px rgba(0, 0, 0, .3);
	}
	
	.input-content {
		padding: 0 60upx;
	}
	
	.input-item {
		display: flex;
		flex-direction: column;
		align-items: flex-start;
		justify-content: center;
		padding: 0 30upx;
		background: #f8f6fc;
		height: 120upx;
		border-radius: 4px;
		margin-bottom: 50upx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.tit {
			height: 50upx;
			line-height: 56upx;
			font-size: $font-sm+2upx;
			color: $font-color-base;
		}
		
		input {
			height: 60upx;
			font-size: $font-base + 2upx;
			color: $font-color-dark;
			width: 100%;
		}
	}

	.confirm-btn {
		width: 630upx;
		height: 76upx;
		line-height: 76upx;
		border-radius: 50px;
		margin-top: 70upx;
		background: $uni-color-primary;
		color: #fff;
		font-size: $font-lg;
		&:after {
			border-radius: 100px;
		}
	}

	.register-section {
		margin-top: 30upx;
		padding: 0 70upx;
		text {
			color: $uni-color-primary;
			margin-left: 10upx;
		}
	}
	
	.oauth-row {
		display: flex;
		flex-direction: row;
		justify-content: center;
		position: relative;
		top: 40upx;
		
		.oauth-image {
			width: 100upx;
			height: 100upx;
			border: 1px solid #dddddd;
			border-radius: 100px;
			margin: 0 40upx;
			background: #ffffff;
			
			image {
				width: 60upx;
				height: 60upx;
				margin: 20upx;
			}
		}
	}
</style>
