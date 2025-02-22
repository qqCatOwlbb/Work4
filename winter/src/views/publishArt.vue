<script setup>
  import { ref } from 'vue';
  import headbar from './headbar.vue';
  import { inject } from 'vue';
  import { useRouter } from 'vue-router';
  const router = useRouter();
  const token = localStorage.getItem('token');
  const userId = localStorage.getItem('userId');
  const content = ref('');
  const title = ref('');

  // 模拟用户信息数据
  const url = "http://120.55.66.81:8080/api";
  async function fetchPublish(title, content, author_id) {

    try {  // 目标 API 地址
      console.log('Request Params:', { title, content, author_id });
      const response = await fetch(`${url}/article/publish?title=${title}&content=${content}&author_id=${author_id}`, {
        method: 'POST',

        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
      });

      const data = await response.json();
      console.log('Publish Success:', data);
      if(data.success){
        router.push({name: 'home'})
      }
      return data;
    } catch (error) {
      console.error('Error in fetchPublish:', error);
      throw error;
    }
  }

  //     请求参数
  //          title String 文章标题
  //          content String 文章内容
  //          author_id int 作者名称

  async function submitContent() {
    console.log(title.value);
    console.log(content.value);
    await fetchPublish(title.value, content.value, userId);
  }

</script>

<template>
  <headbar />
  <div class="background">
    <div class="container">
      <div class="form-group">
        <label for="title">请输入标题</label>
        <input v-model="title" type="text" id="title" placeholder="请输入标题" class="input-field" />
      </div>

      <div class="form-group">
        <label for="content">请输入正文</label>
        <textarea v-model="content" id="content" placeholder="请输入正文" class="textarea-field"></textarea>
        <div class="character-count">
          字数：{{ content.length }} 字
        </div>
      </div>

      <div class="buttons">
        <!-- <button @click="saveDraft" class="btn btn-draft">草稿</button> -->
        <button @click="submitContent" class="btn btn-submit">发布</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .background {
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .container {
    width: 80%;
    padding: 20px;
    border: 1px solid #409EFF;
    /* 深蓝色边框 */
    border-radius: 8px;
    background-color: #ECF5FF;
  }

  .form-group {
    margin-bottom: 15px;
    display: flex;
    flex-direction: column;
  }

  .input-field,
  .textarea-field {
    width: 80%;
    padding: 10px;
    margin-top: 5px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 5px;
    background-color: #ECF5FF;
  }

  .textarea-field {
    height: 150px;
  }

  .character-count {
    font-size: 14px;
    color: #777;
  }

  /* .buttons {
      display: flex;
      justify-content: space-between;
      margin-top: 20px;
    } */

  .btn {
    padding: 10px 20px;
    border-radius: 5px;
    cursor: pointer;
  }

  .btn-draft {
    background-color: #f0f0f0;
    border: 1px solid #ccc;
  }

  .btn-submit {
    background-color: #4CAF50;
    color: white;
    border: none;
    margin-left: 76%;
    /* position: absolute; */
  }

  .btn:hover {
    opacity: 0.8;
  }
</style>