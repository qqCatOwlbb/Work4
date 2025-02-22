<template>
<div class="nav-container">
    <div class="topbar">
      <div>
        <RouterLink to="/home">
        <img class="logo" src="../assets/cbbe2ad5-17eb-4ad6-b688-75e2528f7a6f.png" />
      </RouterLink>
      </div>
      <div class="right-section">

        <div class="selfinf">
          <div class="dropdown" @click="toggleDropdown">
            <span><img class="headimag" src="https://avatars.githubusercontent.com/u/77468756?v=4"></span>
            <div class="dropdown-content" v-if="dropdownVisible">
              <p class="setting">
                <RouterLink to="/MyHome">MyHome</RouterLink>
              </p>
              <p class="setting">
                <RouterLink to="/Setting">我的设置</RouterLink>
              </p>
              <p class="setting" @click="logout">
                <RouterLink to="/logIn">退出登录</RouterLink>
              </p>
            </div>
          </div>
        </div>
        <button class="create-button">
          <RouterLink to="/publishArt">创作 +</RouterLink>
        </button>
      </div>
    </div>
    <div class="divider"></div>
  </div>
</template>

<script setup>
import { RouterLink, RouterView } from "vue-router";
import { ref } from 'vue';

  const dropdownVisible = ref(false);
  const avatar = localStorage.getItem('avatar')

  
  function toggleDropdown() {
    dropdownVisible.value = !dropdownVisible.value;
  }
  function logout(){
    localStorage.clear();
  }
</script>

<style scoped>
  .nav-container {
    width: 100%;
  }

  .topbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    height: 70px;
  }

  .divider {
    width: 100%;
    height: 2px;
    background-color: #818181;
  }

  .logo {
    margin-left: 10vw;
    cursor: pointer;
  }

  .right-section {
    display: flex;
    align-items: center;
    gap: 20px;
    margin-right: 10vw;
  }

  .create-button {
    padding: 8px 16px;
    background-color: #409EFF;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.3s;
    margin-left: 100px;
  }

  .create-button:hover {
    background-color: #66b1ff;

  }

  .selfinf {
    /* margin-right: 10vw; */
    height: 60px;
  }

  .headimag {
    width: 60px;
    height: 60px;
    display: block;
    margin: 0 auto;
    /* 水平居中 */
    border-radius: 50%;
    /* 变为圆形 */
    cursor: pointer;
    /* 鼠标悬停时显示为可点击 */
  }

  .dropdown {
    position: relative;
    display: inline-block;
  }

  .dropdown-content {
    display: block;
    position: absolute;
    background-color: #f9f9f9;
    min-width: 160px;
    box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
    padding: 12px 16px 12px 16px;

    margin: 10px 0px 0 0;
    left: -50px;
    /* 向左移动50px */
    text-align: center;
    /* 文本居中 */
    pointer-events: auto;
    /* display: none; */
  }

  .setting {
    margin: 8px;
    padding: 8px;
    box-shadow: 1px 1px 1px 1px #818181;
    cursor: pointer;
    transition: all 0.3s ease;
  }

  .setting:hover {
    background-color: #ECF5FF;
    color: #409EFF;
    box-shadow: 2px 2px 2px 2px #818181;
  }


</style>