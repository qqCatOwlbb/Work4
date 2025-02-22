<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';

const route = useRoute();
const token = localStorage.getItem('token');
const userId = localStorage.getItem('userId');
const articleId = route.params.id;
const url = "http://120.55.66.81:8080/api";

const currentUserAvatar = ref('');
const commentText = ref('');
const replyText = ref('');
const parentComments = ref([]);
const activeReplyId = ref(null);
const currentSort = ref('hot');

var myHeaders = new Headers();
  myHeaders.append("Content-Type", "application/json");
  myHeaders.append("Authorization", "Bearer " + token);


// 获取当前用户信息
const fetchCurrentUserInfo = async () => {
  try {
    const response = await fetch(`${url}/user/getinfobyid?id=${userId}`, {
      headers: myHeaders
    });
    const data = await response.json();
    if (data.success) {
      currentUserAvatar.value = data.data.avatar;
    }
  } catch (error) {
    console.error('获取用户信息失败:', error);
  }
};

// 获取评论者信息
const fetchCommenterInfo = async (userId) => {
  try {
    const response = await fetch(`${url}/user/getinfobyid?id=${userId}`, {
      headers: myHeaders
    });
    const data = await response.json();
    return data.success ? data.data : null;
  } catch (error) {
    console.error('获取评论者信息失败:', error);
    return null;
  }
};

// 获取父评论
const fetchParentComments = async () => {
  try {
    const response = await fetch(`${url}/comment/parent?article_id=${articleId}`, {
      headers: myHeaders
    });
    const data = await response.json();
    console.log(data.data);
    if (data.success) {
      // 获取每个评论的用户信息
      const commentsWithInfo = await Promise.all(data.data.map(async (comment) => {
        const userInfo = await fetchCommenterInfo(comment.user_id);
        return {
          ...comment,
          avatar: userInfo?.avatar,
          children: [] // 初始化子评论数组
        };
      }));
      parentComments.value = commentsWithInfo;
      
      // 获取每个父评论的子评论
      await Promise.all(parentComments.value.map(fetchChildComments));
    }
  } catch (error) {
    console.error('获取父评论失败:', error);
  }
};

// 获取子评论
const fetchChildComments = async (parentComment) => {
  try {
    const response = await fetch(`${url}/comment/son?parent_comment_id=${parentComment.comment_id}`, {
      headers: myHeaders,
      
    });
    const data = await response.json();
    if (data.success) {
      // 获取每个子评论的用户信息
      const childrenWithInfo = await Promise.all(data.data.map(async (child) => {
        const userInfo = await fetchCommenterInfo(child.user_id);
        return {
          ...child,
          avatar: userInfo?.avatar
        };
      }));
      parentComment.children = childrenWithInfo;
    }
  } catch (error) {
    console.error('获取子评论失败:', error);
  }
};

const updateCharCount = () => {
  if (commentText.value.length > 1000) {
    commentText.value = commentText.value.slice(0, 1000);
  }
};

//发表父评论
const submitComment = async () => {
  if (!commentText.value.trim()){
    
    return;
  }
  console.log(commentText.value);
  // ${commentText}
  try {
    const response = await fetch(`${url}/comment/add?article_id=${articleId}&user_id=${userId}&content=${commentText.value}&parent_comment_id=`, {
      method: 'POST',
      headers: myHeaders,
    });

    const data = await response.json();
    if (data.success) {
      commentText.value = '';
      await fetchParentComments(); // 刷新评论列表
    }
  } catch (error) {
    console.error('提交评论失败:', error);
  }
};

const setSort = (type) => {
  currentSort.value = type;
  // 实现排序逻辑
};

const likeComment = (comment) => {
  comment.isLiked = !comment.isLiked;
  comment.likes += comment.isLiked ? 1 : -1;
};

// 显示回复输入框
const showReplyInput = (commentId) => {
  activeReplyId.value = commentId;
  replyText.value = '';
};

// 取消回复
const cancelReply = () => {
  activeReplyId.value = null;
  replyText.value = '';
};

// 提交回复
const submitReply = async (parentCommentId) => {
  if (!replyText.value.trim()) return;

  try {
    const response = await fetch(`${url}/comment/add?article_id=${articleId}&user_id=${userId}&content=${replyText.value}&parent_comment_id=${parentCommentId}`, {
      method: 'POST',
      headers: myHeaders,
    });

    const data = await response.json();
    if (data.success) {
      replyText.value = '';
      activeReplyId.value = null;
      await fetchParentComments(); // 刷新评论列表
    }
  } catch (error) {
    console.error('提交回复失败:', error);
  }
};

onMounted(async () => {
  await fetchCurrentUserInfo();
  await fetchParentComments();
});
</script>

<template>
  <div class="comment-section">
    <h2 class="comment-title">评论区</h2>

    <!-- 评论输入区域 -->
    <div class="comment-input-area">
      <img class="user-avatar" :src="currentUserAvatar" alt="用户头像" />
      <div class="input-wrapper">
        <textarea 
          class="comment-input" 
          placeholder="请在此发表您的看法" 
          v-model="commentText"
          @input="updateCharCount"
        ></textarea>
        <div class="input-footer">
          <span class="char-count">{{ commentText.length }}/1000字</span>
          <button class="send-btn" @click="submitComment">发送</button>
        </div>
      </div>
    </div>

    <!-- 评论列表 -->
    <div class="comment-list">
      <!-- 排序选项 -->
      <div class="sort-options">
        <span 
          :class="['sort-option', { active: currentSort === 'hot' }]"
          @click="setSort('hot')"
        >按热度</span>
        <span 
          :class="['sort-option', { active: currentSort === 'time' }]"
          @click="setSort('time')"
        >按时间</span>
      </div>

      <!-- 父评论 -->
      <div v-for="comment in parentComments" :key="comment.comment_id" class="comment-item">
        <img class="commenter-avatar" :src="comment.avatar" alt="评论者头像" />
        <div class="comment-content">
          <div class="commenter-info">
            <span class="commenter-name">{{ comment.username }}</span>
            <span class="comment-time">{{ comment.time }}</span>
          </div>
          <p class="comment-text">{{ comment.content }}</p>
          <div class="comment-actions">
            <span class="reply-btn" @click="showReplyInput(comment.comment_id)">回复</span>
            <span class="like-count" @click="likeComment(comment)">
              <i class="like-icon" :class="{ 'liked': comment.isLiked }">❤</i>
              {{ comment.likes }}
            </span>
          </div>

          <!-- 回复输入框 -->
          <div v-if="activeReplyId === comment.comment_id" class="reply-input-area">
            <textarea 
              v-model="replyText" 
              class="reply-input"
              placeholder="请输入回复内容"
            ></textarea>
            <div class="reply-footer">
              <button class="reply-send-btn" @click="submitReply(comment.comment_id)">回复</button>
              <button class="cancel-btn" @click="cancelReply">取消</button>
            </div>
          </div>

          <!-- 子评论列表 -->
          <div class="child-comments" v-if="comment.children && comment.children.length > 0">
            <div v-for="child in comment.children" :key="child.comment_id" class="child-comment-item">
              <img class="commenter-avatar" :src="child.avatar" alt="评论者头像" />
              <div class="comment-content">
                <div class="commenter-info">
                  <span class="commenter-name">{{ child.username }}</span>
                </div>
                <p class="comment-text">{{ child.content }}</p>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.comment-section {
  max-width: 80%;
  margin: 0 auto;
  padding: 20px;
  background-color: #f0f7ff;
  border: 1px solid #409EFF;
  border-radius: 8px;
}

.comment-title {
  font-size: 18px;
  color: #333;
  margin-bottom: 20px;
}

.comment-input-area {
  display: flex;
  gap: 15px;
  margin-bottom: 30px;
  
}

.user-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.input-wrapper {
  flex: 1;
  background: #f0f7ff;
  border-radius: 8px;
  padding: 10px;
  border: 1px solid #409EFF;
  border-radius: 8px;
}

.comment-input {
  width: 100%;
  min-height: 100px;
  border: none;
  background: transparent;
  resize: none;
  outline: none;
  font-size: 14px;
}

.input-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.char-count {
  color: #999;
  font-size: 12px;
}

.send-btn {
  background: #409EFF;
  color: white;
  border: none;
  padding: 5px 15px;
  border-radius: 4px;
  cursor: pointer;
}

.sort-options {
  margin-bottom: 20px;
}

.sort-option {
  margin-right: 20px;
  cursor: pointer;
  color: #666;
}

.sort-option.active {
  color: #409EFF;
  font-weight: bold;
}

.comment-item {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
  
}

.commenter-avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
}

.comment-content {
  flex: 1;
}

.commenter-info {
  margin-bottom: 5px;
}

.commenter-name {
  font-weight: bold;
  margin-right: 10px;
}

.comment-time {
  color: #999;
  font-size: 12px;
}

.comment-text {
  margin: 5px 0;
  color: #333;
}

.comment-actions {
  margin-top: 5px;
}

.like-count {
  cursor: pointer;
  color: #999;
}

.like-icon {
  margin-right: 5px;
  font-size: 14px;
}

.like-icon.liked {
  color: #409EFF;
}

.reply-input-area {
  margin: 10px 0;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
  background-color: #f0f7ff;
}

.reply-input {
  width: 100%;
  min-height: 60px;
  padding: 8px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  resize: vertical;
  margin-bottom: 10px;
  background-color: #f0f7ff;
}

.reply-footer {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
}

.reply-send-btn,
.cancel-btn {
  padding: 5px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.reply-send-btn {
  background: #409EFF;
  color: white;
}

.cancel-btn {
  background: #909399;
  color: white;
}

.child-comments {
  /* margin-left: 40px; */
  margin-top: 10px;
  
}

.child-comment-item {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
  padding: 10px;
  background: #f8f9fa;
  border-radius: 4px;
  background-color: #f0f7ff;
}

.reply-btn {
  color: #409EFF;
  cursor: pointer;
  font-size: 14px;
}

.reply-btn:hover {
  color: #66b1ff;
}
</style>

