<template>
  <div>
    <div style="height:40px">
      <el-switch
        v-model="draggable"
        active-text="开启拖拽"
        inactive-text="关闭拖拽"
      >
      </el-switch>
      <el-button
        v-if="draggable"
        type="success"
        size="mini"
        @click="() => saveDrag()"
        >保存拖拽</el-button
      >
      <el-button type="danger" size="mini"  @click="() => batchDelete()">批量删除</el-button>
    </div>

    <el-tree
      :data="menus"
      :props="defaultProps"
      :expand-on-click-node="false"
      show-checkbox
      node-key="catId"
      :default-expanded-keys="expandendkey"
      :draggable="draggable"
      :allow-drop="allowDrop"
      @node-drop="handleDrop"
      ref="menuTree"
    >
      <span class="custom-tree-node" slot-scope="{ node, data }">
        <span>{{ node.label }}</span>
        <span>
          <el-button
            v-if="node.level <= 2"
            type="text"
            size="mini"
            @click="() => append(data)"
          >
            Append
          </el-button>
          <el-button
            v-if="node.childNodes.length == 0"
            type="text"
            size="mini"
            @click="() => remove(node, data)"
            >Delete</el-button
          >
          <el-button type="text" size="mini" @click="() => edit(data)">
            Edit
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogFormVisible"
      :close-on-click-modal="false"
    >
      <el-form :model="category">
        <el-form-item label="菜单名称">
          <el-input v-model="category.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="菜单图标">
          <el-input v-model="category.icon" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="菜单单位">
          <el-input
            v-model="category.productUnit"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cancel()">取 消</el-button>
        <el-button type="primary" @click="turnDialog(dialogTitle)"
          >确 定</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>
<script>
export default {
  data() {
    return {
      // changeDialog: "",
      pcId: [],
      draggable: false,
      updateNodes: [],
      maxLevel: "0",
      dialogTitle: "",
      menus: [],
      category: {
        name: "",
        parentCid: 0,
        catLevel: 1,
        showStatus: 1,
        sort: 0,
        icon: "",
        catId: null,
        productUnit: "",
        productCount: 0
      },
      expandendkey: [],
      dialogFormVisible: false,
      defaultProps: {
        children: "children",
        label: "name"
      }
    };
  },
  methods: {
    //批量删除
    batchDelete() {
      /*
      getCheckedNodes()
      (leafOnly, includeHalfChecked) 接收两个 boolean 类型的参数，
      1. 是否只是叶子节点，默认值为 false
      2. 是否包含半选节点，默认值为 false */
      let checkIds = [];
      let checkNodes = this.$refs.menuTree.getCheckedNodes();
      for (let i = 0; i < checkNodes.length; i++) {
        checkIds.push(checkNodes[i].catId);
      }
      console.log("checkNodes",checkNodes)
      console.log("checkIds",checkIds)
      this.$confirm(`此操作将批量删除 【${checkIds}】 菜单, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/product/category/delete"),
            method: "post",
            data: this.$http.adornData(checkIds, false)
          }).then(({ data }) => {
            this.$message({
              type: "success",
              message: "删除成功!"
            });
            this.getMenus();
            // this.expandendkey = checkIds[0];
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },

    //拖拽节点函数
    allowDrop(draggingNode, dropNode, type) {
      this.countNodeLevel(draggingNode);
      let deep = Math.abs(this.maxLevel - draggingNode.level) + 1;
      if (type == "inner") {
        return deep + dropNode.level <= 3;
      } else {
        return deep + dropNode.parent.level <= 3;
      }
    },
    countNodeLevel(node) {
      if (node.childNodes != null && node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          if (node.childNodes[i].level > this.maxLevel) {
            this.maxLevel = node.childNodes[i].level;
          }
          this.countNodeLevel(node.childNodes[i]);
        }
      }
    },
    //拖拽后的节点数据
    handleDrop(draggingNode, dropNode, dropType, ev) {
      //获取当前节点的父节点
      let pcId = 0;
      let siblings = null;
      if (dropType == "inner") {
        pcId = dropNode.data.catId;
        siblings = dropNode.childNodes;
      } else {
        pcId =
          dropNode.data.parentCid == undefined ? 0 : dropNode.data.parentCid;
        siblings = dropNode.parent.childNodes;
      }
      console.log("tree drop: ", draggingNode, dropNode, dropType);

      this.pcId.push(pcId);

      //当前拖拽节点的最新顺序

      for (let i = 0; i < siblings.length; i++) {
        if (siblings[i].data.catId == draggingNode.data.catId) {
          let catLevel = draggingNode.Level;
          if (siblings[i].Level != draggingNode.tLevel) {
            //当前节点的层级发生变化
            catLevel = siblings[i].Level;
            // 修改子节点的层级
            this.updateChildNodeLevel(siblings[i]);
          }
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i,
            parentCid: pcId
          });
        } else {
          this.updateNodes.push({ catId: siblings[i].data.catId, sort: i });
        }
      }
      console.log("updateNodes:", this.updateNodes);
      //当前拖拽节点的最新层级
    },

    saveDrag() {
      this.$http({
        url: this.$http.adornUrl("/product/category/update/sort"),
        method: "post",
        data: this.$http.adornData(this.updateNodes, false)
      }).then(({ data }) => {
        this.$message({
          type: "success",
          message: "菜单顺序修改成功!"
        });
        // this.dialogFormVisible = false;
        this.getMenus();
        this.expandendkey = this.pcId;
        this.updateNodes = [];
        this.maxLevel = "0";
        this.pcId = [];
      });
    },
    updateChildNodeLevel(node) {
      if (node.children.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          var cNode = node.children[i].data;
          this.updateNodes.push({
            catId: cNode.catId,
            catLevel: node.childNodes[i].level
          });
          this.updateChildNodeLevel(node.childNodes[i]);
        }
      }
    },
    turnDialog(data) {
      console.log(data);
      if (data == "添加菜单") {
        this.addCategory();
      }
      if (data == "修改菜单") {
        this.editCategory();
      }
    },
    cancel() {
      this.dialogFormVisible = false;
      this.category.name = "";
      this.category.productUnit = "";
      this.category.icon = "";
    },
    getMenus() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get"
      }).then(({ data }) => {
        this.menus = data.page;
      });
    },
    append(data) {
      this.dialogTitle = "添加菜单";
      console.log("Append.data:", data);
      this.changeDialog = "addDialog";
      this.dialogFormVisible = true;
      this.category.parentCid = data.catId;
      this.category.catLevel = data.catLevel * 1 + 1;
      this.category.showStatus = 1;
      this.category.catId = null;
      this.category.icon = null;
      this.category.productUnit = null;
      this.category.name = null;
    },
    addCategory() {
      console.log("添加的菜单:", this.category);
      this.$http({
        url: this.$http.adornUrl("/product/category/save"),
        method: "post",
        data: this.$http.adornData(this.category, false)
      }).then(({ data }) => {
        this.$message({
          type: "success",
          message: "添加成功!"
        });
        this.dialogFormVisible = false;
        this.getMenus();
        this.expandendkey = [this.category.parentCid];
      });
    },
    remove(node, data) {
      var ids = [data.catId];
      this.$confirm(`此操作将删除 【${data.name}】 菜单, 是否继续?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/product/category/delete"),
            method: "post",
            data: this.$http.adornData(ids, false)
          }).then(({ data }) => {
            this.$message({
              type: "success",
              message: "删除成功!"
            });
            this.getMenus();
            this.expandendkey = [node.parent.data.catId];
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    edit(data) {
      this.dialogTitle = "修改菜单";
      this.changeDialog = "editDialog";
      this.dialogFormVisible = true;
      console.log("Edit.data.datId:", data.catId);
      this.$http({
        url: this.$http.adornUrl(`/product/category/info/${data.catId}`),
        method: "get"
      }).then(({ data }) => {
        this.category = data.data;
        console.log("Edit.data:", data);
      });
      // console.log("Edit.data-this.category:", this.category);
    },
    editCategory() {
      var { catId, name, icon, productUnit } = this.category;
      console.log("修改的菜单:", this.category);
      console.log("Edit.data-this.category:", this.category);
      this.dialogFormVisible = false;
      this.$http({
        url: this.$http.adornUrl("/product/category/update"),
        method: "post",
        data: this.$http.adornData({ catId, name, icon, productUnit }, false)
      }).then(({ data }) => {
        this.$message({
          type: "success",
          message: "修改成功!"
        });
        this.dialogFormVisible = false;
        this.getMenus();
        this.expandendkey = [this.category.parentCid];
        this.category.name = "";
      });
    }
  },
  created() {
    this.getMenus();
  }
};
</script>
