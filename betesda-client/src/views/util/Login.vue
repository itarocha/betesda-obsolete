<template>
  <div>
    <b-row>
      <b-col offset="3" cols="6">
        <b-card class="shadow"
                header-tag="header"
                header-bg-variant="primary"
                header-text-variant="white"
                footer-tag="footer">
            
          <div slot="header" class="font-weight-bold">Login</div>
            
            <div class="card-text">
              <b-form>
                  <b-form-row>
                    <b-col>
                      <b-form-group label="Usuário:"
                                    label-for="exampleInput1">
                        <b-form-input id="exampleInput1"
                                      type="email"
                                      ref="edtUserName"
                                      v-model="form.username"
                                      required
                                      placeholder="Enter com o email">
                        </b-form-input>
                      </b-form-group>
                    </b-col>  
                  </b-form-row>  

                  <b-form-row>
                    <b-col>
                      <b-form-group id="exampleInputGroup2"
                                    label="Senha:"
                                    label-for="exampleInput2">
                        <b-form-input id="exampleInput2"
                                      type="password"
                                      v-model="form.password"
                                      required
                                      placeholder="Entre com a Senha">
                        </b-form-input>
                      </b-form-group>
                    </b-col>
                  </b-form-row>  



                  <b-form-row>
                    <b-col>
                    </b-col>
                  </b-form-row>

              </b-form>

            </div>

          <div style="text-align:right;" slot="footer">
            <b-button type="submit" variant="primary" @click.stop="login" size="sm" class="text-uppercase font-weight-bold px-4" >Login</b-button>
          </div>
        </b-card>
      </b-col>
    </b-row>

  </div>
</template>

<script>

//import DialogoConfirmacao from './DialogoConfirmacao.vue'

export default {

  name: 'Login',
  /*
  components:{
    DialogoConfirmacao
  },
  */

  created(){
  },

  mounted(){
    this.$store.dispatch('setAcao','Login')
    setTimeout(() => {
        this.$refs.edtUserName.focus()
      }, 500)
  },

  data: () =>({
    dados: [],

    form : {
      username : null,
      password : null
    },

  }),

  methods: {

    login(){
      this.$store.dispatch('retrieveToken',
      {
        username: this.form.username,
        password : this.form.password
      }).then(response => {
        //this.$router.push({name: 'hospedagens'})
        //console.log(response.data)

        //petra.showMessageSuccess('Usuário logado com sucesso')
        petra.showMessageInfo('Usuário logado com sucesso')

        this.$router.push({name: 'teste'}) // hospedagens
      }).catch(error =>{
        console.log(error)
        petra.showMessageError('Usuário ou senha inválidos')
      })
    },

  }
}
</script>