<template>
  <div>
    <b-form @submit="onSubmit" @reset="onReset" v-if="show">
      <b-container>
        <b-form-row>
          <b-col cols="4">
            <b-form-group id="exampleInputGroup1"
                          label="Endereço de Email:"
                          label-for="exampleInput1"
                          description="We'll never share your email with anyone else.">
              <b-form-input id="exampleInput1"
                            type="email"
                            v-model="form.email"
                            required
                            placeholder="Enter email">
              </b-form-input>
            </b-form-group>
          </b-col>  

          <b-col cols="6">
            <b-form-group id="exampleInputGroup2"
                          label="Nome:"
                          label-for="exampleInput2">
              <b-form-input id="exampleInput2"
                            type="text"
                            v-model="form.name"
                            :formatter="fmtMaiusculas"
                            required
                            placeholder="Entre com o Nome">
              </b-form-input>
            </b-form-group>
          </b-col>

          <b-col cols="2">
            <b-form-group id="exampleInputGroup3"
                          label="Food:"
                          label-for="exampleInput3">
              <b-form-select id="exampleInput3"
                            :options="foods"
                            required
                            v-model="form.food">
              </b-form-select>
            </b-form-group>
          </b-col>
        </b-form-row>  


        <b-form-row>
          <b-col>
            <b-form-group label="Data Entrada:"
                          label-for="exampleInput2">
              <b-form-input type="date"
                            v-model="form.dataEntrada"
                            required
                            placeholder="Enter name">
              </b-form-input>
            </b-form-group>
          </b-col>


          <b-col>
            <b-form-group label="RG:"
                          label-for="exampleInput2">
              <b-form-input type="text"
                            v-model="form.rg"
                            :formatter="fmtLetrasENumeros"
                            required
                            placeholder="Entre com o número da RG">
              </b-form-input>
            </b-form-group>
          </b-col>

          <b-col>
            <b-form-group label="CPéFo:"
                          label-for="exampleInput2">
              <b-form-input type="text"
                            v-model="form.cpf"
                            v-mask="'###.###.###-##'"
                            required
                            placeholder="Entre com o número do CPF">
              </b-form-input>
            </b-form-group>
          </b-col>
        </b-form-row>


        <b-form-row>
          <b-col>
            <b-form-group id="exampleGroup4">
              <b-form-checkbox-group v-model="form.checked" id="exampleChecks">
                <b-form-checkbox value="me">Check me out</b-form-checkbox>
                <b-form-checkbox value="that">Check that out</b-form-checkbox>
              </b-form-checkbox-group>
            </b-form-group>

          </b-col>
        </b-form-row>




        <b-form-row>
          <b-col>
          </b-col>
        </b-form-row>

        <b-form-row>
          <b-col>
            <b-button type="submit" style="background-color: #ef0; color:#000;" variant="primary" >Submit</b-button>
            <b-button type="reset" variant="danger">Reset</b-button>
          </b-col>
        </b-form-row>

      </b-container>



    </b-form>
  </div>
</template>

<script>
  import {mask} from 'vue-the-mask'

export default {
  directives: {mask},

  data () {
    return {
      form: {
        email: '',
        name: '',
        dataEntrada: null,
        rg: '',
        cpf: '',
        outrocpf : '',
        food: null,
        checked: []
      },
      foods: [
        { text: 'Select One', value: null },
        'Carrots', 'Beans', 'Tomatoes', 'Corn'
      ],
      show: true
    }
  },
  methods: {
    fmtMaiusculas(value, event){
      return petra.removerAcentos(value).toUpperCase()
    },

    fmtLetrasENumeros(value, event){
      return petra.letrasENumeros(value).toUpperCase()
    },

    onSubmit (evt) {
      evt.preventDefault();
      alert(JSON.stringify(this.form));
    },
    onReset (evt) {
      evt.preventDefault();
      /* Reset our form values */
      this.form.email = '';
      this.form.name = '';
      this.form.food = null;
      this.form.checked = [];
      /* Trick to reset/clear native browser form validation state */
      this.show = false;
      this.$nextTick(() => { this.show = true });
    }
  }
}
</script>
<style>
  .oua{
    background-color: #ef0; color:#000;
  }
</style>
