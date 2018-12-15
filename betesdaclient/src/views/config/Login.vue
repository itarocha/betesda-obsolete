<template>
  <div>

    <v-container fluid fill-height>
        <v-layout align-center justify-center>
          <v-flex xs12 sm8 md4>
            <v-card class="elevation-5">
              <v-toolbar dark class="teal darken-3">
                <v-toolbar-title>Login</v-toolbar-title>
              </v-toolbar>
              <v-card-text>
                <v-form>
                  <v-text-field prepend-icon="person" name="login" label="Login" type="text" v-model="form.username"></v-text-field>
                  <v-text-field id="password" prepend-icon="lock" name="password" label="Password" type="password" v-model="form.password"></v-text-field>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer></v-spacer>
                <v-btn dark color="cyan darken-4">Login</v-btn>
              </v-card-actions>
            </v-card>
          </v-flex>
        </v-layout>
      </v-container>

  </div>
</template>

<script>

import DialogoConfirmacao from './DialogoConfirmacao.vue'

export default {

  name: 'Login',
  components:{
    DialogoConfirmacao
  },

  created(){
    this.getData()
  },

  mounted(){
    this.$store.dispatch('setAcao','Login')
  },

  data: () =>({
    dados: [],

    form : {
      username : null,
      password : null
    },

  }),

  computed: {
    descricaoItemExclusao(){
      var descricao = this.form.descricao ? this.form.descricao : 'Não selecionado';
      return 'Deseja realmente excluir "'+descricao+'"?'
    }
    
  },

  methods: {
      getData(evt) {
        let uri = petra.base_uri+"/app/tipo_servico"; 
        axios.get(uri).then(response => {
          this.dados = response.data;
        });      
      },

  }
}
</script>