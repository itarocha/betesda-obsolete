<template>
    <el-card shadow="never" class="body" :style="{backgroundColor: colorStatusItem(model.hospedagem.statusHospedagem)}">
        <div class="fw-bold titulo">{{model.hospedagem.nome}}
            <el-tooltip content="Editar" placement="bottom" :open-delay="toolTipDelay">
                <el-button type="primary" plain size="mini" circle @click="onEditPessoa(model.hospedagem.id)">
                <i class="fas fa-pencil-alt"></i>
                </el-button>
            </el-tooltip>
        </div>

        <el-row type="flex">
            <el-col>Fone: <span class="fw-bold">{{model.hospedagem.telefone}}</span></el-col>
            <el-col>Cidade: <span class="fw-bold">{{model.hospedagem.cidadeUf}}</span></el-col>
        </el-row>    

        <el-row type="flex">
            <el-col>Destinação: <span class="fw-bold">{{model.hospedagem.destinacao}}</span></el-col>
            <el-col>Tipo Hóspede: <span class="fw-bold">{{model.hospedagem.tipoHospedeDescricao}}</span></el-col>
        </el-row>    


        <el-row type="flex">
            <el-col>Utilização: <span class="fw-bold">{{model.hospedagem.tipoUtilizacaoDescricao}}</span></el-col>
            <el-col><span v-if="model.hospedagem.tipoUtilizacao == 'T'">Leito: <span class="fw-bold">{{model.leito.quartoNumero}}-{{model.leito.leitoNumero}}</span></span></el-col>
        </el-row>    


        <div>Status: <span class="fw-bold">{{model.hospedagem.statusHospedagem}}</span>
            <el-tooltip content="Ver Detalhes" placement="bottom" :open-delay="toolTipDelay">
                <el-button type="primary" plain size="mini" circle @click="onShowHospedagemInfo(model.hospedagem.hospedagemId)">
                <i class="fas fa-info"></i>
                </el-button>
            </el-tooltip>
       </div>                      
    </el-card>
</template>

<script>

export default {
    name: "CardHospede",

    props: {
        model: {
            type: Object,
            default: {
                hospedagem: {
                    id: 0,
                    hospedagemId: 0,
                    nome: 'Unknow',
                    cidadeUf: 'Unknow',
                    telefone: '---',
                    statusHospedagem: 'Unknow',
                    tipoUtilizacaoDescricao: 'Unknow',
                    tipoUtilizacao: 'T',
                    tipoHospedeDescricao : 'Unknow',
                    destinacao : 'Unknow'
                },
                leito: {
                    quartoNumero: 0,
                    leitoNumero: 0
                }
            }
        }
    },

    data:() => ({

        toolTipDelay: 500,

    }),

    methods : {

        colorStatusItem(status){
            if (status == 'ABERTA'){
                return '#FFF9C4' // teal darken-2
            } else if (status == 'ENCERRADA'){
                return '#d3dce6' //'#E0E0E0' // blue-grey lighten-1
            } else if (status == 'VENCIDA'){
                return '#FFCCBC' // red accent-4
            }
            return 'blue'
        },

        onEditPessoa(pessoaId){
            this.$emit('editPessoa', pessoaId)
        }, 

        onShowHospedagemInfo(hospedagemId){
            this.$emit('showHospedagemInfo', hospedagemId)
        }

    },

}
</script>

<style scoped>

    .body{
        background: #FFF9C4;
        color: #455A64;
        font-size: 10pt; 
        padding: 5px; 
        margin: 8px;
        width: 420px; 
        line-height: 1.5em;
    }

    .fw-bold{
        font-weight: bold;        
    }

    .titulo{
        font-size: 1.1em;
    }

</style>