<template>
    <div class="background"></div>
    <div class="container">
        <div class="regBox" ref="regUI">
            <h2 class="head-1">Don't have an account?</h2>
            <p class="para-1">sign up now and start managing your contacts</p>
            <button class="btn" id="btnSwitchReg" @click="switchToReg">regsiter</button>
        </div>
        <div class="logInBox" ref="logUI">
            <h2 class="head-2">Have an account?</h2>
            <p class="para-2">log in to your account and start managing your contacts</p>
            <button class="btn" id="btnSwitchLog" @click="switchToLog">log in</button>
        </div>
    </div>
    <!-- <form @submit.prevent="handleRegister"> -->
    <div class="regUi" ref="regUI">
        <div class="regInput">
            <h2 class="head_reg">REGISTER</h2>
            <input type="username" placeholder="username" v-model="username" >
            <input type="password" placeholder="Password" v-model="password">
            <input class="btn_reg" type="submit" value="Register" @click="handleRegister">
        </div>
    </div>
    <!-- </form> -->
    <!-- <form @submit.prevent="handleLogin"> -->
    <div class="logUi" ref="logUI">
        <div class="logInput">
            <h2 class="head_log">LOG IN</h2>
            <input type="username" placeholder="username" v-model="username">
            <input type="password" placeholder="Password" v-model="password">
            <input class="btn_log" type="submit" value="LOG IN" @click="handleLogin">
        </div>
    </div>
    <!-- </form> -->
</template>
<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();
const regUI = ref(null);
const logUI = ref(null);
const username = ref('');
const password = ref('');
const Token = ref('');
const switchToReg = () => {
    regUI.value.style.visibility = "visible";
    regUI.value.style.opacity = "1";
    logUI.value.style.visibility = "hidden";
    logUI.value.style.opacity = "0";
};

const switchToLog = () => {
    logUI.value.style.visibility = "visible";
    logUI.value.style.opacity = "1";
    regUI.value.style.visibility = "hidden";
    regUI.value.style.opacity = "0";
};

const url = "http://120.55.66.81:8080/api";
    async function fetchRegister() {
        console.log(username.value);
        console.log(password.value);
      const response = await fetch(`${url}/register?username=${username.value}&password=${password.value}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        
      });
      const data = await response.json();
      Token.value = data.data;
      username.value = '';
      password.value = '';
      console.log(Token.value);
      return Token.value;
    }

    function handleRegister() {
        fetchRegister();
        switchToLog();
    }
    //login
    async function fetchLogin() {
        console.log(username.value);
        console.log(password.value);
      const response = await fetch(`${url}/login?username=${username.value}&password=${password.value}`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
      });
      const data = await response.json();
      Token.value = data.data;
      username.value = '';
      password.value = '';
      console.log(data);
      return Token.value;
    }

    async function handleLogin() {

        // Assuming the token is in response.token
        const test = await fetchLogin();
        localStorage.setItem('token',test);
        console.log(localStorage.getItem('token'));
        if(localStorage.getItem('token')!='null'){
            router.push('/home'); // Redirect to /home after storing the token
        }else{
            alert('用户名或密码错误');
        }
    }

</script>
<style scoped>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    outline: 0;
    transition: all 0.5s ease;
}

.background {
    width: 100vw;
    height: 100vh;
    display: grid;
    place-items: center;
    background-image: linear-gradient(to bottom right, #613333, #31155e);
    background-size: cover;
    background-position: center;
    filter: blur(2px);
}

.container {
    position: absolute;
    width: 80vw;
    height: 50vh;
    background-color: #32393E;
    color: #fefefe;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);
    display: flex;
    left: 50%;
    top: 50%;
    transform: translate(-50%, -50%);
}

.regBox,
.logInBox {
    width: 50%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    padding: 80px 40px 40px 60px;
    transition: all 0.3s;
}

.head-1,
.head-2 {
    padding-bottom: 20px;
}

.para-1,
.para-2 {
    padding-bottom: 40px;
}

.btn {
    width: 150px;
    height: 50px;
    border: 1px solid #fefefe;
    border-radius: 5px;
    font-weight: bold;
    outline: 0;
    display: flex;
    justify-content: center;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s;
    text-transform: uppercase;
}

.btn:hover {
    background-color: #8b8b8b;
}

.regUi,
.logUi {
    width: 36vw;
    height: 60vh;
    display: flex;
    flex-direction: column;
    justify-content: center;
    padding: 40px 40px 40px 40px;
    transition: all 0.3s;
    position: absolute;
    top: 50%;
    transform: translatey(-50%);
    background-color: #ffffff;
    color: #268fc0;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.3);

}

.regUi {
    left: calc(10% + 35px);
    transition: opacity 0.35s, visibility 0.35s;
    align-items: center;
    visibility: hidden;
    opacity: 0;
    /* display: ""; */
}

.logUi {
    right: calc(10% + 35px);
    transition: opacity 0.35s, visibility 0.35s;
    visibility: visible;
    opacity: 1;
    /* display: none; */
}

.regUi:active {
    opacity: 1;
    /* visibility: visible; */
    transition: opacity 0.5s ease, visibility 0.5s ease;
    /* transform: translateX(20vw); */

}

.regInput,
.logInput {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    width: 100%;
    height: 100%;
}

.head_reg {
    margin-bottom: 50px;
}

.head_log {
    margin-bottom: 40px;
}

input {
    border: none;
    border-bottom: 1px solid #909a9e;
    width: 100%;
    height: 50px;
}

.btn_reg,
.btn_log {
    border-bottom: none;
    cursor: pointer;
    margin-top: 20px;
    width: 30%;
    align-self: self-end;
    background-color: #2791c2;
    border-radius: 10px;
    color: #ffffff;
}
</style>