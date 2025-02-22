<script setup>
  import { ref, onMounted } from 'vue';
  import { useRouter } from 'vue-router';
  import headbar from './headbar.vue';

  const router = useRouter();
  const token = localStorage.getItem('token');
  const userId = localStorage.getItem('userId');
  const url = "http://120.55.66.81:8080/api";

  const userInfo = ref({});
  const isEditing = ref(false);
  const isEditingBio = ref(false);
  const newUsername = ref('');
  const newPassword = ref('');
  const newBio = ref('');
  const fileInput = ref(null);

  var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "Bearer " + token);

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const response = await fetch(`${url}/user/getinfo`, {
        method: 'GET',
        headers: myHeaders
      });
      const data = await response.json();
      if (data.success) {
        userInfo.value = data.data;
        newBio.value = data.data.bio || '';
      }
    } catch (error) {
      console.error('获取用户信息失败:', error);
    }
  };

  // 处理信息修改
  const handleUpdate = async () => {
      console.log(newUsername.value || null);
      console.log(newPassword.value || null);
      console.log(userInfo.value.username);
      if(newUsername.value==userInfo.value.username){
        newUsername.value = '';
      }
      const response = await fetch(`${url}/update?id=${userId}&username=${newUsername.value}&password=${newPassword.value}&avatar=&bio=`, {
        method: 'POST',
        headers: myHeaders,
      });

      const data = await response.json();
      if (data.success) {
        if (data.data === 100 || data.data === 200) {
          alert('修改成功，请重新登录');
          localStorage.removeItem('token');
          router.push('/login');
        } else {
          alert('修改成功');
          isEditing.value = false;
          fetchUserInfo();
        }
      } else {
        alert(data.message);
      }
  };

  // 处理简介修改
  const handleBioEdit = async () => {
    if (isEditingBio.value) {
      try {
        const response = await fetch(`${url}/update?id=${userId}&username=&password=&avatar=&bio=${newBio.value}`, {
          method: 'POST',
          headers: myHeaders,
        });

        const data = await response.json();
        if (data.success) {
          alert('简介修改成功');
          fetchUserInfo();
        } else {
          alert(data.message);
        }
      } catch (error) {
        console.error('更新简介失败:', error);
        alert('更新失败，请重试');
      }
    }
    isEditingBio.value = !isEditingBio.value;
  };

  // 处理头像修改
  const handleFileChange = async (event) => {
    const file = event.target.files[0];
    
    console.log(file);
    if (file) {
      const reader = new FileReader();
      reader.onload = async (e) => {
        try {
          console.log(newUsername.value);
          console.log(newPassword.value)
          const response = await fetch(`${url}/update?id=${userId}&username=&password=&avatar=${URL.createObjectURL(file)}&bio=`, {
            method: 'POST',
            headers: myHeaders,
          });

          const data = await response.json();
          if (data.success) {
            alert('头像修改成功');
            fetchUserInfo();
          } else {
            alert(data.message);
          }
        } catch (error) {
          console.error('更新头像失败:', error);
          alert('更新失败，请重试');
        }
      };
      reader.readAsDataURL(file);
    }
  };

  // 切换编辑状态
  const toggleEdit = () => {
    if (isEditing.value) {
      handleUpdate();
    } else {
      isEditing.value = true;
      newUsername.value = userInfo.value.username;
      newPassword.value = '';
    }
  };

  const cancel = () =>{
    isEditing.value = false;
  }
  onMounted(() => {
    fetchUserInfo();
  });
</script>

<template>
  <headbar />
  <div class="setting-page">
    <div class="left-section">
      <div class="avatarBox">
        <img class="avatar" :src="userInfo.avatar" alt="用户头像" />
        <input 
          type="file" 
          ref="fileInput" 
          style="display: none" 
          accept="image/*"
          @change="handleFileChange"
        />
      </div>
      <h2 class="username">{{ userInfo.username }}</h2>
      <p class="description" id="changeAvatar" @click="$refs.fileInput.click()">修改我的头像</p>
      <div class="bio-section">
        <textarea 
          v-if="isEditingBio" 
          v-model="newBio" 
          class="bio-input" 
          placeholder="请输入简介"
        ></textarea>
        <p v-else>简介：{{ userInfo.bio || '暂无简介' }}</p>
      </div>
      <div class="button-group">
        <button class="edit-btn" @click="handleBioEdit">{{ isEditingBio ? '保存简介' : '修改简介' }}</button>
        <button class="back-btn" @click="$router.push('/home')">返回主页</button>
      </div>

      <div class="info-section">
        
        <div class="form-group" v-if="isEditing">
          <label for="nickname">修改昵称</label>
          <input type="text" id="nickname" v-model="newUsername" placeholder="请输入新昵称" />
          <p class="hint">昵称长度为6-16个字符，字母数字均可</p>
        </div>

        <div class="form-group" v-if="isEditing">
          <label for="password">修改密码</label>
          <input type="password" id="password" v-model="newPassword" placeholder="请输入新密码" />
          <p class="hint">密码长度为6-16个字符，字母数字均可</p>
        </div>

        <div class="user-info">
          <div class="info">
            <div class="button-group">
            <button class="edit-btn edit-info" @click="toggleEdit">
              {{ isEditing ? '保存修改' : '修改信息' }}
            </button>
            <button class="edit-btn edit-info" @click="cancel" v-if="isEditing">
              取消修改
            </button>
          </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .setting-page {
    max-width: 80%;
    margin: 20px auto;
    padding: 20px;
    background: #ECF5FF;
    /* 浅蓝色背景 */
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    display: flex;
    border: 1px solid #409EFF;
    /* 深蓝色边框 */
  }

  #changeAvatar {
    cursor: pointer;
  }

  .left-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    width: 100%;
    margin: 0 auto;
  }

  .avatarBox {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
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
    margin: 10px 0;
  }

  .description {
    color: #666;
    font-size: 14px;
    cursor: pointer;
    margin: 10px 0;
  }

  .bio-section {
    width: 100%;
    max-width: 500px;
    margin: 20px 0;
    text-align: center;
  }

  .bio-input {
    width: 100%;
    min-height: 80px;
    padding: 8px;
    margin-bottom: 10px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    resize: vertical;
  }

  .button-group {
    display: flex;
    gap: 10px;
    margin: 20px 0;
  }

  .info-section {
    width: 100%;
    max-width: 500px;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #409EFF;
  }

  .form-group {
    margin-bottom: 20px;
  }

  label {
    display: block;
    margin-bottom: 8px;
    font-weight: bold;
  }

  input {
    width: 100%;
    padding: 10px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    margin-bottom: 5px;
  }

  .hint {
    color: #999;
    font-size: 12px;
  } 

  .edit-btn, .back-btn {
    padding: 8px 16px;
    background-color: #409EFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  .info{
    display: flex;
    justify-self: center;
  }
  .edit-btn:hover, .back-btn:hover {
    background-color: #66b1ff;
  }
</style>