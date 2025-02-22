<template>
  <headbar />
  <div v-for="article in articles" :key="article.article_id">
    <div class="article" v-if="article&&article.article_id==articleId" @click="handleArticleClick(articleId)">
      <div class="titleBar">
        <h1>{{ article.title }}</h1>
        <div class="like-button" :class="{ 'liked': isLiked }" @click="toggleLike">
          <svg class="heart" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" width="24" height="24">
            <path
              d="M12 21.35l-1.45-1.32C5.4 15.36 2 12.28 2 8.5 2 5.42 4.42 3 7.5 3c1.74 0 3.41.81 4.5 2.09C13.09 3.81 14.76 3 16.5 3 19.58 3 22 5.42 22 8.5c0 3.78-3.4 6.86-8.55 11.54L12 21.35z" />
          </svg>
        </div>
      </div>
      <p class="authorName">Author: {{ article.username }}</p>
      <div class="info">
        <!-- <p>作者: {{ article.author_id }}</p> -->
        <p>阅读量: {{ article.view_count }}</p>
        <p>点赞数: {{ article.like_count }}</p>
        <p>Date: {{ formatDate(article.created_at) }}</p>
      </div>
      <div class="content" v-html="article.content"></div>
      <!-- 删除按钮 -->
      <div class="delete-button-container" v-if="showDeleteButton&&article.article_id==articleId">
        <button class="delete-btn" @click="handleDelete">
          删除文章
        </button>
      </div>
    </div>
  </div>
  <commentArea />

</template>

<script setup>
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import headbar from './headbar.vue';
  import commentArea from './comment.vue'
  const route = useRoute();
  const router = useRouter();
  const token = localStorage.getItem('token');
  const articleId = route.params.id;
  const currentPage = ref(1);     // 当前页数
  const pageSize = ref(10);       // 每页显示多少篇文章
  const showDeleteButton = ref(false);
  const isLiked = ref(false);
  const user_id = ref('');
  const userId = localStorage.getItem('userId');
  
  console.log(articleId);
  const handleClick = () => {
    likeButton.classList.toggle("liked");
  };
  /*
    获取文章内容
    */
  const articles = ref([]);
  const url = "http://120.55.66.81:8080/api";
  const fetchArticles = async () => {
    const response = await fetch(`${url}/article/select/bylike?page=${currentPage.value}&pageSize=${pageSize.value}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
    });
    const data = await response.json();
    articles.value = data.data;
  };
  onMounted(async () => {
    await checkLikeStatus();
    fetchArticles();
    // 在组件挂载时检查路由来源
    const fromMyHome = route.query.from === 'myHome';
    showDeleteButton.value = fromMyHome;
    updateViewCount(articleId);
  });

  const formatDate = (date) => {
    return new Date(date).toLocaleDateString('zh-CN');
  };

  /* 
  删除文章的方法
  */
  const handleDelete = async () => {
    if (confirm('确定要删除这篇文章吗？')) {
      try {
        const response = await fetch(`${url}/article/delete?article_id=${articleId}`, {
          method: 'DELETE',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });

        const data = await response.json();
        if (data.success) {
          alert('文章删除成功');
          router.push('/MyHome'); // 删除成功后返回个人主页
        } else {
          alert('删除失败：' + data.message);
        }
      } catch (error) {
        console.error('删除文章时出错:', error);
        alert('删除失败，请稍后重试');
      }
    }
  };


  /* 
  检查并更新阅读量
  */
  const updateViewCount = async (articleId) => {
    // 获取用户已读文章列表
    const viewedArticles = JSON.parse(localStorage.getItem('viewedArticles') || '[]');
    
    // 检查文章是否已被当前用户阅读
    if (!viewedArticles.includes(articleId)) {
      try {
        const response = await fetch(`${url}/article/view?article_id=${articleId}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${token}`
          }
        });

        const data = await response.json();
        
        if (data.success) {
          // 将文章ID添加到已读列表
          viewedArticles.push(articleId);
          localStorage.setItem('viewedArticles', JSON.stringify(viewedArticles));
          console.log('阅读量更新成功');
        }
      } catch (error) {
        console.error('更新阅读量失败:', error);
      }
    }
  };

  // 在文章点击事件中调用 handleArticleClick
  const handleArticleClick = (articleId) => {
    updateViewCount(articleId);
    // 其他点击处理逻辑...
  };

  /*
   检查是否已点赞
   */
  const checkLikeStatus = async () => {
    try {
      const response = await fetch(`${url}/user/mylike?user_id=${userId}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        }
      });
      const data = await response.json();
      console.log(data.data);
      if (data.success && data.data) {
        // 检查当前文章是否在点赞列表中
        isLiked.value = data.data.some(article => article.id == articleId);
        console.log('点赞状态:', isLiked.value);
      }
    } catch (error) {
      console.error('获取点赞状态失败:', error);
    }
  };

  // 点赞/取消点赞功能
  const toggleLike = async () => {
    try {
      const method = isLiked.value ? 'DELETE' : 'POST';
      const response = await fetch(`${url}/article/like?article_id=${articleId}&user_id=${userId}`, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
      });

      const data = await response.json();
      
      if (data.success) {
        isLiked.value = !isLiked.value;
        // 更新点赞数
        if (isLiked.value) {
          // article.like_count++;
          console.log('like_count+1')
        } else {
          // article.like_count--;
          console.log('like_count-1')
        }
        console.log(isLiked.value ? '点赞成功' : '取消点赞成功');
      } else {
        console.error('操作失败:', data.message);
      }
    } catch (error) {
      console.error('点赞操作失败:', error);
    }
  };
</script>

<style scoped>
  .article {
    border: 1px solid #ccc;
    padding: 16px;
    margin: 16px auto;
    width: 80%;
    background-color: #ECF5FF;
    border: 1px solid #409EFF;
    /* 深蓝色边框 */
    border-radius: 8px;
  }

  .titleBar {
    display: flex;
    align-items: center;
  }

  .authorName {
    margin-left: 0px;
  }

  .info {
    margin-bottom: 16px;
    display: flex;
    align-items: center;


    margin-left: -20px;
  }

  p {
    margin: 0 20px;
  }

  .content {
    margin-top: 12px;
  }

  /* 设置球形按钮 */
  .like-button {
    width: 40px;
    height: 40px;
    background-color: #ff4081;
    /* 粉色背景 */
    border-radius: 50%;
    /* 使其变为圆形 */
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    transition: transform 0.3s, background-color 0.3s;
    right: 200px;
    position: absolute;
  }

  /* 按钮点击时的动画 */
  .like-button:active {
    transform: scale(0.9);
  }

  /* SVG 爱心图标 */
  .heart {
    fill: white;
    width: 50%;
    height: 50%;
    transition: fill 0.3s;
  }

  /* 点赞后的颜色变化 */
  .liked .heart {
    fill: rgb(255, 0, 0);
  }

  /* 当按钮被点击时，添加 liked 类 */


  .delete-btn {
    padding: 10px 20px;
    background-color: #ff4444;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
    /* right: 200px; */
    /* position: absolute; */
    margin-left: 91%;
  }

  .delete-btn:hover {
    background-color: #ff6666;
  }

  /* 添加动画效果 */
  @keyframes likeAnimation {
    0% { transform: scale(1); }
    50% { transform: scale(1.2); }
    100% { transform: scale(1); }
  }

  .like-button:active {
    animation: likeAnimation 0.3s ease;
  }
</style>