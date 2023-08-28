<template>
  <div class="tableContainer">
    <div class="table" ref="table">
      <slot name="table"/>
    </div>
    <div class="page" v-if="isPage">
      <div class="count" >
        <p>合计</p>
        <p v-for="item in summation" :key="item.label">{{ item.title }}：<span>{{ item.value }}</span>{{ item.unit }}</p>
      </div>
      <el-pagination
        :page-size="pageSize"
        :current-page="pageNum"
        :total="total"
        :page-sizes="[20, 50, 100, 200]"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handlePageNum"/>
    </div>
  </div>
</template>

<script>
export default {
  name: "countTable",
  props: {
    summation: {
      type: Array,
      default: () => []
    },
    //单页数据量
    pageSize: {
      type: Number,
      default: 20
    },
    //当前页码
    pageNum: {
      type: Number,
      default: 1
    },
    //分页数据总数
    total: {
      type: Number,
      default: 0
    },
    isPage: {
      type: Boolean,
      default: true
    }
  },
  data() {
    return {
      tableContainer: '',
      tableHeight: ''
    }
  },
  methods: {
    handleSizeChange(pageSize) {
      this.$emit('handleChange', pageSize, this.pageNum);
    },
    handlePageNum(pageNum) {
      this.$emit('handleChange', this.pageSize, pageNum);
    },
  }
}
</script>

<style lang="scss" scoped>

.tableContainer {
  width: 100%;
  height: 100%;
  $fontSize: 12px;
  //ie浏览器兼容
  box-sizing: content-box;
  -moz-box-sizing: inherit;
  -webkit-box-sizing: inherit;

  .table {
    height: calc(100% - 38px);
    overflow: hidden;

    ::v-deep .el-table {
      border-right: 1px solid #dfe6ec;
      color: #909399;

      & > .el-table__header-wrapper {
        & > table {
          tr:first-of-type {
            th {
              background: rgba(204, 204, 204, 0.18);
              color: #909399;
              font-size: 12px;
              text-align: center;
              height: 34px !important;
              padding: 5px 0 !important;

              .cell {
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
              }
            }
          }

          tr:nth-of-type(2) {
            th {
              background: #fff;
              color: #909399;
              font-size: 12px;
              text-align: center;
              padding: 0 !important;
              height: 34px;
              .el-input__inner {
                border: none;
                padding: 0;
                height: 20px;
              }

              .el-input__suffix {
                width: 12px;

                .el-input__icon {

                  line-height: 23px;
                }
              }
            }
          }

          & > colgroup {
            col {
              &:last-of-type {
                width: 17px !important;
              }
            }
          }
        }
      }

      .el-table__body-wrapper {
        overflow: auto !important;

        .el-table__row {
          td {
            text-align: center;
            padding: 5px 0 !important;


          }
          .operation {
            .cell {
              display: flex;
              justify-content: space-around;
              width: 100%;
              .el-button {
                width: 100%;
                height: 100%;
                position: relative;
                top: 0;
                right: 0;
                margin: 0 2px;
              }
            }
          }
        }
      }
    }
  }

  .page {
    height: 38px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .count {
      width: 100%;
      display: flex;
      p {
        border: 1px solid #dfe6ec;
        padding: 5px 10px;
        border-right: none;
        color: rgb(19, 19, 19);
        letter-spacing: 1px;
        font-size: 16px;
        font-weight: 600;

        span {
          color: red;
        }
      }

      p:last-of-type {
        border-right: 1px solid #dfe6ec;
      }

      p:first-of-type {
        background-color: rgba(204, 204, 204, 0.18);
      }
    }

    ::v-deep .el-pagination {
      height: 28px;
      margin-top: 5px;
      font-size: $fontSize;

      & > .el-pagination__total {
        font-size: $fontSize;
      }

      & > .el-pagination__sizes {
        .el-input {
          input {
            font-size: $fontSize;
            height: 26px;
          }
        }
      }

      button {
        background-color: transparent;
      }

      & > .el-pager {
        li {
          font-size: $fontSize;
          background-color: transparent;
        }
      }

      & > .el-pagination__jump {
        font-size: $fontSize;

        .el-input {
          input {
            font-size: $fontSize;
            height: 26px;
          }
        }
      }
    }
  }
}
</style>
