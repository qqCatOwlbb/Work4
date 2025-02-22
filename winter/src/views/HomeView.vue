<script setup>
  import { ref, onMounted, watch } from 'vue';
  import { useRouter } from 'vue-router';
  import headbar from './headbar.vue';
  const router = useRouter();
  const token = localStorage.getItem('token');
  console.log(token);
  const currentPage = ref(1);     // 当前页数
  const pageSize = ref(10);       // 每页显示多少篇文章
  const articles = ref([]);
  const userInfo = ref([]);
  const url = "http://120.55.66.81:8080/api";
  console.log(token);
  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "Bearer " + token);

  const fetchArticles = async () => {
    const response = await fetch(`${url}/article/select/bylike?page=${currentPage.value}&pageSize=${pageSize.value}`, {
      method: 'GET',
      headers: myHeaders,
    });
    const data = await response.json();
    articles.value = data.data;

  };
  
  /*
  / 获取用户信息
  */
  async function fetchInfo() {
    const response = await fetch(`${url}/user/getinfo?Authorization=Bearer ${token}`, {
      method: 'GET',
      headers: myHeaders,
    });
    const data = await response.json();
    userInfo.value = data.data;
    return userInfo.value;
  }

  //监听userInfo
  watch(userInfo, (newValue) => {
    if (newValue && newValue.id) {
      localStorage.setItem('userId', newValue.id);//存储用户id
    }
  }, { deep: true });

  onMounted(() => {
    fetchInfo();
    fetchArticles();
  });
  const navigateToArticle = (id) => {
    router.push({ name: 'articles', params: { id } });
  };

</script>

<template>
  <headbar />
  <div class="container">

    <div class="content-container">
      <div class="headbar">
        <span class="headword">文章榜</span>
        <span class="headword">最新</span>
      </div>

      <div class="article-list">
        <div class="article-item" v-for="article in articles" :key="article.article_id">
          <div class="article-content">
            <h2 class="article-title" @click="navigateToArticle(article.article_id)">{{ article.title }}</h2>
            <p class="article-summary">{{ article.content }}</p>
          </div>
          <div class="article-image">
            <!-- <img :src="article.image" alt="文章配图"> -->
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
  /* 内容 */
  .content-container {
    display: flex;
    flex-direction: column;
    /* 改为纵向排列 */
    align-items: center;
    /* 居中对齐 */
  }

  .headbar {
    width: 80vw;
    height: 50px;
    margin-top: 70px;
    background-color: #ECF5FF;
    border: 1px solid #409EFF;
    display: flex;
    align-items: center;
    gap: 30px;
    padding-left: 20px;
  }

  .headword {
    font-size: 16px;
    /* 字体大小 */
    color: #409EFF;
    /* 文字颜色 */
    font-weight: 500;
    /* 字体粗细 */
    cursor: pointer;
    /* 鼠标悬停效果 */
  }

  .headword:hover {
    color: #66b1ff;
    /* 悬停时的颜色 */
  }

  .article-list {
    width: 80vw;
    margin: 20px auto;
  }

  .article-item {
    display: flex;
    justify-content: space-between;
    padding: 20px 0;
    border-bottom: 1px solid #eee;
  }

  .article-content {
    flex: 1;
    margin-right: 20px;
  }

  .article-title {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
    cursor: pointer;
  }

  .article-title:hover {
    color: #409EFF;
  }

  .article-summary {
    font-size: 14px;
    color: #666;
    line-height: 1.6;
  }

  .article-image {
    width: 150px;
    height: 100px;
    overflow: hidden;
  }

  .article-image img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    border-radius: 4px;
  }
</style>