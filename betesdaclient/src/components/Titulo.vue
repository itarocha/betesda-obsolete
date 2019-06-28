<template>
  <div>
    <v-toolbar color="green lighten-3" card height="50px">
      <v-toolbar-title>{{texto}}</v-toolbar-title>
      <v-spacer></v-spacer>
      <slot></slot>
    </v-toolbar>
  </div>
</template>

<script>

export default {
  name: "Titulo",

  props: {
    texto : String,
  },
};
</script>