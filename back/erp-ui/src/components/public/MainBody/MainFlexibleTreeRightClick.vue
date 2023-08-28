<template>
  <div class="mainBody">
    <div class="mainLeft" :class="{pageLeftStyle:flag}">
      <div class="containerTree">
        <div class="inputClear">
          <el-input
            size="mini"
            placeholder="请输入内容"
            v-model="filterText"
            clearable>
          </el-input>
        </div>
        <el-scrollbar>
          <el-tree
            class="filter-tree"
            empty-text=" "
            :data="data"
            :props="defaultProps"
            :default-expand-all="defaultExpand"
            :filter-node-method="filterNode"
            @node-click="handleNodeClick"
            @node-contextmenu="rightClick"
            :highlight-current="true"
            expand-on-click-node
            icon-class="el-icon-folder-opened"
            ref="tree">
          </el-tree>
          <div class="RightClick">
            <slot name="RightClick"/>
          </div>
        </el-scrollbar>
      </div>
      <div class="shrinkageDiv" @click="divClick()">
        <div class="pic" :class="picClassname"></div>
      </div>
    </div>
    <div class="mainRight" :class="{pageRightContainer:flag}">
      <slot name="mainRight"/>
    </div>
  </div>
</template>

<script>
export default {
  name: "MainFlexibleTreeRightClick",

  props: {
    data: {
      type: Array,
      default: () => []
    },
    defaultProps: {
      type: Object,
      default: () => {
      }
    },
    defaultExpand: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      picClassname: 'shrinkageLeft',
      flag: false,
      filterText: '',
    }
  },
  watch: {
    filterText(val) {
      console.log('val', val)
      this.$refs.tree.filter(val);
    }
  },
  methods: {
    divClick() {
      this.flag = !this.flag
      if (this.flag) {
        this.picClassname = 'shrinkageRight'
      } else {
        this.picClassname = 'shrinkageLeft'
      }
    },
    filterNode(value, data) {
      if (!value) return true;
      return data[this.defaultProps.label].indexOf(value) !== -1;
    },
    handleNodeClick(val, node, component) {
      this.$emit('handleNodeClick', val, node, component)
    },
    rightClick(event, object, value,) {
      this.$emit('rightClick', event, object, value,)
    }
  }
}
</script>

<style lang="scss" scoped>
.mainBody {
  width: 100%;
  height: 100%;
  display: flex;

  .mainLeft {
    height: 100%;
    width: 16%;
    position: relative;
    overflow: hidden;
    border-right: 1px solid #ccc;

    .containerTree {
      width: 100%;
      height: 100%;

      ::v-deep .el-input {
        width: 90%;
        padding: 20px 0 0 20px;
        font-size: 12px;

        .el-input__inner {
          padding: 0 15px;
        }
        .el-input__suffix{
          top: 12px;
        }
      }

      ::v-deep .el-scrollbar {
        height: calc(100% - 75px);

        .el-scrollbar__wrap {
          margin: 0 !important;
          overflow-x: hidden;
          .el-scrollbar__view {
            margin: 10px 15px;

            .el-tree {
              & > .el-tree-node {
                min-width: 100%;
                display: inline-block;
              }
            }
          }


        }
      }
    }

    .RightClick {
      position: fixed;
      display: block;
      z-index: 99999;
      //border-radius: 3px;
      //box-shadow: 0 0.5em 1em 0 rgba(0, 0, 0, .1);

      ::v-deep .right-menus {
        font-size: 14px;
        background-color: #fff;
        width: 100px;
        border: solid 1px #F5F7FA;
        //border: 1px solid rgba(0, 0, 0, 0.15);
        box-shadow: 0 6px 12px rgba(0, 0, 0, 0.175);

        a {
          width: 98px;
          height: 28px;
          line-height: 28px;
          text-align: center;
          display: block;
          color: #1a1a1a;
          padding: 2px;

          &:hover {
            //background: #ccc;
            background-color: #66b1ff;
            color: #fff;
          }
          //&:active {
          //  //background: #ccc;
          //  background-color: red;
          //  color: #fff;
          //}
        }
      }

      a {
        text-decoration: none;
      }
    }

    ////显示滚动条
    ::v-deep .el-scrollbar .el-scrollbar__wrap {
      overflow-x: hidden;
      margin: 0 !important;
      width: 100%;
    }

    ::v-deep .el-tree > .el-tree-node {
      min-width: 100%;

      display: inline-block;

      .el-tree-node__label {
        font-size: 13px !important;
      }
    }

    .pic {
      position: absolute;
      width: 8px;
      height: 8px;
      left: 0;
      right: 0;
      top: 0;
      bottom: 0;
      margin: auto;
      cursor: pointer;
    }

    .shrinkageLeft {
      border-right: 8px solid #FFFFFF;
      border-top: 8px solid transparent;
      border-bottom: 8px solid transparent;
    }

    .shrinkageRight {
      border-left: 8px solid #FFFFFF;
      border-top: 8px solid transparent;
      border-bottom: 8px solid transparent;
    }

    .shrinkageDiv {
      background-color: #1890ff;
      cursor: pointer;
      right: 0;
      bottom: 0;
      margin: auto;
      top: 0;
      height: 200px;
      width: 8px;
      border-radius: 20px;
      position: absolute;
      z-index: 99;
    }
  }

  .mainRight {
    height: 100%;
    width: 84%;
    padding: 10px 10px 0 10px;
    box-sizing: border-box;
  }

  .pageLeftStyle {
    width: 10px;

    .containerTree {
      display: none;
    }
  }

  .pageRightContainer {
    width: calc(100% - 15px);
  }
}

</style>
