<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    :width="dialogWidth"
    :append-to-body="appendToBody"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
    class="dialog-style">
    <slot name="dialogBody"/>
    <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleSave" size="mini">{{ submitText }}</el-button>
        <el-button @click="handleCancel" size="mini">{{ cancelText }}</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: "commonDialog",
  props: {
    //弹窗标题
    dialogTitle: {
      type: String,
      default: ''
    },
    //是否显示 Dialog
    dialogVisible: {
      type: Boolean,
      default: false
    },
    //弹窗宽度
    dialogWidth: {
      type: String,
      default: '30%'
    },
    //是否为二级弹窗
    appendToBody: {
      type: Boolean,
      default: false
    },
    //  保存弹窗
    submitText: {
      type: String,
      default: '确定'
    },
    //  取消弹窗
    cancelText: {
      type: String,
      default: '取消'
    },

  },
  methods: {
    handleSave() {
      this.$emit('handleSave');
    },
    handleCancel() {
      this.$emit('handleCancel');
    },
  }
}
</script>

<style scoped>

</style>
