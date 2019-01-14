<template>
  <div>
    <v-dialog v-model="dialogVisible" width="500">
        <v-card>
        <v-card-title dark class="white--text cyan darken-4">
          {{titulo}}
        </v-card-title>
        <v-card-text>
          {{mensagem}}{{message}}
        </v-card-text>
        <v-divider></v-divider>
        <v-card-actions class="grey lighten-4"> 
          <v-spacer></v-spacer>
          <v-btn small dark color="cyan darken-4" @click.native="ok(true)">
            Ok
          </v-btn>
          <v-btn small color="secondary" @click.native="cancel(false)">
            Cancelar
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>

export default {
  name: 'DialogoConfirmacao',
  
  props: {
    titulo : String,
    mensagem : String,
  },

  data: () =>({
    dialogVisible : false
  }),

  created(){
  },

  methods: {
    // publico
    openDialog(message){
      if (message){
        this.message = message
      }
      this.dialogVisible = true
    },

    ok(value){
      this.dialogVisible = false
      this.$emit('ok', value)
    },

    cancel(value){
      this.dialogVisible = false
      this.$emit('cancel')
    }
  }
}
</script>