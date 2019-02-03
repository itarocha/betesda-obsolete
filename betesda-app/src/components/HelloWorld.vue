<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <el-tabs type="border-card">
      <el-tab-pane label="User">User</el-tab-pane>
      <el-tab-pane label="Config">Config</el-tab-pane>
      <el-tab-pane label="Role">Role</el-tab-pane>
      <el-tab-pane label="Task">Task</el-tab-pane>
    </el-tabs>    
  </div>
</template>

<script>
export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: 'Bem vindo ao Vue.js App'
    }
  }
}
</script>

<style scoped>
</style>
