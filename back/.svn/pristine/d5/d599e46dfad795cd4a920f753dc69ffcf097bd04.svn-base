<template>
  <div>
  <div class="navbar">
    <hamburger id="hamburger-container" :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar" />
    <top-nav id="topmenu-container" class="topmenu-container" />
    <div class="right-menu">
      <template v-if="device!=='mobile'">
<!--        <search id="header-search" class="right-menu-item" />-->

<!--        <el-tooltip content="源码地址" effect="dark" placement="bottom">-->
<!--          <ruo-yi-git id="ruoyi-git" class="right-menu-item hover-effect" />-->
<!--        </el-tooltip>-->

<!--        <el-tooltip content="文档地址" effect="dark" placement="bottom">-->
<!--          <ruo-yi-doc id="ruoyi-doc" class="right-menu-item hover-effect" />-->
<!--        </el-tooltip>-->

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

<!--        <el-tooltip content="布局大小" effect="dark" placement="bottom">-->
<!--          <size-select id="size-select" class="right-menu-item hover-effect" />-->
<!--        </el-tooltip>-->

      </template>

      <el-dropdown class="avatar-container right-menu-item hover-effect" trigger="click">
        <div class="avatar-wrapper">
          <span style="margin-right: 5px;">{{ getNickName }}</span>
          <i class="el-icon-arrow-down" />
        </div>
        <el-dropdown-menu slot="dropdown">
          <router-link to="/user/profile">
            <el-dropdown-item>个人中心</el-dropdown-item>
          </router-link>
          <el-dropdown-item @click.native="sytem">
            <span>系统授权</span>
          </el-dropdown-item>
<!--          <el-dropdown-item @click.native="setting = true">
            <span>布局设置</span>
          </el-dropdown-item>-->
          <el-dropdown-item divided @click.native="logout">
            <span>退出登录</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
  <div class="breadcrumbDiv">
    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
  </div>
    <!--  系统授权信息  -->
    <el-dialog title='系统授权信息'
               :visible.sync="detailDialog" width="50%" class="dialog-style detailDialog"
               :close-on-click-modal="false" :close-on-press-escape="false" :destroy-on-close="true">
      <div class="detailsInfo">
        <el-descriptions v-model="systemData">
          <el-descriptions-item label="授权系统">{{systemData.systemName}}</el-descriptions-item>
          <el-descriptions-item label="授权公司">{{systemData.systemCorp}}</el-descriptions-item>
          <el-descriptions-item label="授权负责人">{{systemData.person}}</el-descriptions-item>
          <el-descriptions-item label="被授权公司">{{systemData.buyCorp}}</el-descriptions-item>
          <el-descriptions-item label="系统版本号">
            <el-tag size="small">{{systemData.systemVersion}}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="授权类型">{{systemData.type}}</el-descriptions-item>
          <el-descriptions-item label="到期日期" v-if="systemData.type != 0">{{systemData.date}}</el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import TopNav from '@/components/TopNav'
import Hamburger from '@/components/Hamburger'
import Screenfull from '@/components/Screenfull'
import SizeSelect from '@/components/SizeSelect'
import Search from '@/components/HeaderSearch'
import RuoYiGit from '@/components/RuoYi/Git'
import RuoYiDoc from '@/components/RuoYi/Doc'
import ScrollPane from '@/layout/components/TagsView/ScrollPane'
import {check} from "@/api/dongle";

export default {
  data() {
    return {
      detailDialog: false,
      systemData:{},
    };
  },
  components: {
    ScrollPane,
    Breadcrumb,
    TopNav,
    Hamburger,
    Screenfull,
    SizeSelect,
    Search,
    RuoYiGit,
    RuoYiDoc
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'device'
    ]),
    setting: {
      get() {
        return this.$store.state.settings.showSettings
      },
      set(val) {
        this.$store.dispatch('settings/changeSetting', {
          key: 'showSettings',
          value: val
        })
      }
    },
    topNav: {
      get() {
        return this.$store.state.settings.topNav
      }
    },
    getNickName:{
      get(){
        return localStorage.getItem("nickName")
      }
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$store.dispatch('LogOut').then(() => {
          location.href = '/index';
        })
      }).catch(() => {});
    },
    sytem() {

      check().then(res => {
        if (res.code === 200) {
          this.detailDialog = true;
          this.systemData = JSON.parse(res.data);
        }else {
          this.$message.error(res.msg);
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background-color: #fff;

  .hamburger-container {
    line-height: 46px;
    height: 50px;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color:transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
    margin-top: 50px;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .errLog-container {
    display: inline-block;
    vertical-align: top;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        position: relative;
        display: flex;
        align-items: center;
        font-size: 14px;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
.breadcrumbDiv {
  padding-left: 10px;
  width: 100%;
  height: 50px;
  background-color: #FFFFFF;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);
  border-bottom: 1px solid rgba(0, 21, 41, .08);
  border-top: 1px solid rgba(0, 21, 41, .08);
}
</style>
