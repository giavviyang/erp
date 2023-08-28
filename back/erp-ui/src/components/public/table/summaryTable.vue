<template>
  <div class="tableContainer">
    <div class="table">
      <slot name="table"/>
    </div>
    <div class="page">
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
  name: "summaryTable",
  props: {
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
    overflow: auto;
    ::v-deep .el-table {
      color: #909399;
      border-right: 1px solid #dfe6ec;
      table {
        .cell {
          text-align: center;
          padding: 0 10px;
        }

        th, td {
          padding: 5px 0;
        }
      }

      & > .el-table__header-wrapper {
        & > table {
          tr:first-of-type {
            th {
              background: rgba(204, 204, 204, 0.18);
              color: #909399;
              font-size: 12px;
              text-align: center;
              height: 34px;
              padding: 5px 0 !important;
              .cell{
                white-space:nowrap;
                overflow:hidden;
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
        }
      }

      .el-table__body-wrapper {
        border-bottom: none;
        .el-table__row {
          td {
            width: auto;
            padding: 5px 0 !important;
          }
        }
      }

      //合计行滚动条在表格最底部显示
      .el-table--scrollable-x .el-table__body-wrapper {
        overflow-x: hidden !important;
        z-index: 2 !important;
      }

      .el-table__footer-wrapper {
        overflow-x: auto;
        border-top: 1px solid #f4f4f4;
      }
    }
  }

  .page {
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: flex-end;

    ::v-deep .el-pagination {
      height: 28px;
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
