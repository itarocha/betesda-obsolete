      <v-stepper v-model="e1" class="elevation-0">
        <v-stepper-header class="elevation-1">
          <v-stepper-step :complete="e1 > 1" step="1">Configurações iniciais</v-stepper-step>
          <v-divider></v-divider>
          <v-stepper-step :complete="e1 > 2" step="2">Seleção de Leitos</v-stepper-step>
          <v-divider></v-divider>
          <v-stepper-step step="3">Acomodações dos Hóspedes</v-stepper-step>
        </v-stepper-header>

        <v-stepper-items>
          <v-stepper-content step="1">


          </v-stepper-content>

          <v-stepper-content step="2">

          <v-container grid-list-md text-xs-center>
              <v-layout row wrap>
                <v-flex xs6 sm6 md6>
                  
                  <dialogo-selecao-pessoa ref="dlgSelecaoPessoa" @selecionar="onSelecionarPessoa"></dialogo-selecao-pessoa>

                  <v-card class="elevation-0">
                    <v-toolbar card height="45px" class="amber lighten-4">
                      Seleção de Leitos
                    </v-toolbar>                    

                    <v-card-text >
                      <v-container grid-list-md>
                        <v-layout wrap>
                          <v-flex xs12 sm6 md6>
                            <v-select label="Destinação de Hospedagem" :items="itensDestinacaoHospedagem" v-model="formOpcoes.destinacaoHospedagem"></v-select>
                          </v-flex>

                          <v-flex xs12 sm6 md6>
                            <v-select label="Tipo de Utilização" :items="itensUtilizacao" v-model="formOpcoes.tipoUtilizacao"></v-select>
                          </v-flex>
                        </v-layout>
                      </v-container>
                    </v-card-text>
                  </v-card>
                </v-flex>

                <v-flex xs6 sm6 md6>
                  <v-card class="elevation-0">
                    <v-toolbar card height="45px" class="amber lighten-4">
                      Leitos Selecionados
                      <v-spacer></v-spacer>
                    </v-toolbar>                    
                    <v-card-text class="px-0">
                    </v-card-text>
                  </v-card>
                </v-flex>

                <v-flex xs12>
                  <v-card class="elevation-0">
                    <v-card-actions>
                      <v-btn fab class="white--text cyan darken-4" @click="e1 = 1" :disabled="false">
                         <v-icon dark>chevron_left</v-icon>
                      </v-btn>
                      <v-spacer></v-spacer>
                      <v-btn fab class="white--text cyan darken-4" @click="e1 = 3" :disabled="!passo1ok">
                        <v-icon dark>chevron_right</v-icon>
                      </v-btn>
                    </v-card-actions>

                  </v-card>
                </v-flex>

              </v-layout>
          </v-container> 

          </v-stepper-content>

          <v-stepper-content step="3">
            <v-card class="mb-5" color="grey lighten-1" height="250px">
            
            </v-card>

            <v-btn color="primary" @click="e1 = 1">Avançar</v-btn>
            <v-btn flat @click="dialogIncluirHospedagem = false">Cancelar</v-btn>
          </v-stepper-content>
        </v-stepper-items>
      </v-stepper>


            <v-flex xs12>
              <v-card class="elevation-0">
                <v-card-actions>
                <!--
                  <v-btn fab class="white--text cyan darken-4" @click="e1 = 1" :disabled="false">
                      <v-icon dark>chevron_left</v-icon>
                  </v-btn>
                  -->
                  <v-spacer></v-spacer>
                  <v-btn fab class="white--text cyan darken-4" @click="e1 = 2" :disabled="!passo1ok">
                    <v-icon dark>chevron_right</v-icon>
                  </v-btn>
                </v-card-actions>

              </v-card>
            </v-flex>
