<script setup>
  import { ref, onMounted, watch, provide } from 'vue';
  import { useRouter } from 'vue-router';
  import headbar from './headbar.vue';
  const router = useRouter();
  const token = localStorage.getItem('token');
  // 模拟用户信息数据

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "Bearer " + token);

  // const id = ref('');
  // const author_id = ref(1);
  const user_id = ref('');
  const userInfo = ref([]);
  const myArt = ref([]);
  const likedArt = ref([]);

  const url = "http://120.55.66.81:8080/api";
  async function fetchInfo() {
    const response = await fetch(`${url}/user/getinfo?Authorization=Bearer ${token}`, {
      method: 'GET',
      headers: myHeaders,
    });
    const data = await response.json();
    userInfo.value = data.data;
    // provide('user', userInfo.id);
    return userInfo.value;
  }

  // console.log(newValue.id);
  async function fetchMyArt(userId) {
    const response = await fetch(`${url}/user/getart?author_id=${userId}`, {
      method: 'GET',
      headers: myHeaders,
    });
    const data = await response.json();
    myArt.value = data.data;

    console.log(myArt.value);
  }

  async function fetchLikedArt(userId) {
    const response = await fetch(`${url}/user/mylike?user_id=${userId}`, {
      method: 'GET',
      headers: myHeaders,
    });
    const data = await response.json();
    likedArt.value = data.data;
    console.log(likedArt.value);
  }

  onMounted(async () => {
    // try {
    const userInfo = await fetchInfo(); // 等待 fetchInfo 完成
    console.log(userInfo.id);
    user_id.value = userInfo.id;

    console.log(user_id.value);
    const userId = userInfo.id; // 提取用户 ID
    await fetchLikedArt(userId);
    await fetchMyArt(userId); // 将用户 ID 作为参数传递给第二个 fetch
    // } catch (error) {
    //   console.error('Error in onMounted:', error);
    // }
  });
  // 当前显示的类型：'articles' 或 'likes'
  const currentTab = ref('articles');


  const navigateToArticle = (id) => {
    router.push({ name: 'articles', params: { id }, query: { from: 'myHome' } });
  };
  // 切换标签的方法
  const switchTab = (tab) => {
    currentTab.value = tab;
  };
</script>

<template>
  <headbar />
  <div class="my-home">
    <!-- 用户信息区域 -->
    <div class="user-profile">
      <div class="profile-header">
        <div class="profile-info">
          <img class="avatar" src="https://avatars.githubusercontent.com/u/77468756?v=4" alt="用户头像">
          <div class="user-info">
            <h2 class="username">{{ userInfo.username }}</h2>
            <p class="register-date">{{ userInfo.created_at }}注册</p>
          </div>
        </div>
        <button class="settings-btn">
          <RouterLink to="/Setting">设置</RouterLink>
        </button>
      </div>
    </div>

    <!-- 文章区域 -->
    <div class="content-section">
      <div class="tabs">
        <div class="tab-item" :class="{ active: currentTab === 'articles' }" @click="switchTab('articles')">
          文章 <span class="count">({{ myArt.length }})</span>
        </div>
        <div class="tab-item" :class="{ active: currentTab === 'likes' }" @click="switchTab('likes')">
          点赞 <span class="count">({{ likedArt.length }})</span>
        </div>
      </div>

      <!-- 文章列表 -->
      <div v-if="currentTab === 'articles'">
        <div class="article-list" v-if="myArt.length > 0">
          <div class="article-item" v-for="article in myArt" :key="article.id">
            <div class="article-content">
              <h3 class="article-title" @click="navigateToArticle(article.article_id)">{{ article.title }}</h3>
              <p class="article-summary">{{ article.content }}</p>
            </div>
            <!-- <div class="article-map">
              <img :src="article.mapImage" alt="">
            </div> -->
          </div>
        </div>
        <div class="empty-state" v-else>
          <!-- <img class="empty-icon" src="../assets/empty-article.png" alt="暂无文章"> -->
          <p class="empty-text">暂无文章</p>
        </div>
      </div>

      <!-- 点赞文章列表 -->
      <div v-if="currentTab === 'likes'">
        <div class="article-list" v-if="likedArt.length > 0">
          <div class="article-item" v-for="article in likedArt" :key="article.id">
            <div class="article-content">
              <h3 class="article-title" @click="navigateToArticle(article.id)">{{ article.title }}</h3>
              <p class="article-summary">{{ article.content }}</p>
            </div>
            <!-- <div class="article-map">
              <img :src="article.mapImage" alt="">
            </div> -->
          </div>
        </div>
        <div class="empty-state" v-else>
          <!-- <img class="empty-icon" src="../assets/empty-like.png" alt="暂无点赞"> -->
          <p class="empty-text">暂无点赞内容</p>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .my-home {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
  }

  .user-profile {
    background: #ECF5FF;
    /* 浅蓝色背景 */
    border: 1px solid #409EFF;
    /* 深蓝色边框 */
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
  }

  .profile-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .profile-info {
    display: flex;
    align-items: center;
    gap: 20px;
  }

  .avatar {
    width: 80px;
    height: 80px;
    border-radius: 50%;
    object-fit: cover;
  }

  .username {
    font-size: 20px;
    font-weight: bold;
    margin-bottom: 5px;
  }

  .register-date {
    color: #666;
    font-size: 14px;
  }

  .settings-btn {
    padding: 8px 16px;
    background-color: #409EFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }

  .content-section {
    background: #ECF5FF;
    /* 浅蓝色背景 */
    border: 1px solid #409EFF;
    /* 深蓝色边框 */
    border-radius: 8px;
    padding: 20px;
  }

  .tabs {
    display: flex;
    gap: 30px;
    border-bottom: 1px solid #eee;
    padding-bottom: 15px;
    margin-bottom: 20px;
  }

  .tab-item {
    font-size: 16px;
    cursor: pointer;
    color: #666;
    padding: 0 10px;
    transition: all 0.3s ease;
  }

  .tab-item:hover {
    color: #409EFF;
  }

  .tab-item.active {
    color: #409EFF;
    font-weight: bold;
    position: relative;
  }

  .tab-item.active::after {
    content: '';
    position: absolute;
    bottom: -15px;
    left: 0;
    width: 100%;
    height: 2px;
    background-color: #409EFF;
  }

  .count {
    color: #666;
    font-weight: normal;
  }

  .article-item {
    display: flex;
    justify-content: space-between;
    gap: 20px;
    padding: 20px 0;
    border-bottom: 1px solid #eee;
  }

  .article-content {
    flex: 1;
  }

  .article-title {
    font-size: 18px;
    font-weight: bold;
    margin-bottom: 10px;
    color: #333;
    cursor: pointer;
  }

  .article-title:hover {
    color: #409EFF;
  }

  .article-summary {
    color: #666;
    font-size: 14px;
    line-height: 1.6;
  }

  .article-map {
    width: 200px;
    height: 120px;
    background: #f5f5f5;
    border-radius: 4px;
    overflow: hidden;
  }

  .article-map img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 60px 0;
    text-align: center;
  }

  .empty-icon {
    width: 120px;
    height: 120px;
    margin-bottom: 16px;
    opacity: 0.6;
  }

  .empty-text {
    color: #909399;
    font-size: 14px;
    margin-bottom: 16px;
  }

  .create-btn,
  .browse-btn {
    padding: 8px 24px;
    background-color: #409EFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.3s;
  }

  .create-btn:hover,
  .browse-btn:hover {
    background-color: #66b1ff;
  }
</style>