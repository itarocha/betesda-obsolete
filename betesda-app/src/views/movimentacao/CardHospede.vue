<template>
    <el-card shadow="never" :class="body" :style="{backgroundColor: colorStatusItem(hospede.statusHospedagem)}">

        <div class="fw-bold titulo">{{hospede.nome}}
            <el-tooltip content="Editar" placement="bottom" :open-delay="toolTipDelay">
                <el-button type="primary" plain size="mini" circle @click="onEditPessoa(hospede.pessoaId)">
                <i class="fas fa-pencil-alt"></i>
                </el-button>
            </el-tooltip>
        </div>

        <div>Cidade de Origem: <span class="fw-bold">{{hospede.cidadeUf}}</span></div>
        <div>Fone: <span class="fw-bold">{{hospede.fone}}</span></div>
        <div>Utilização: <span class="fw-bold">{{hospede.tipoUtilizacaoDescricao}}</span>
        <span v-if="hospede.tipoUtilizacao == 'T'">Leito: <span class="fw-bold">{{hospede.quartoNumero}}-{{hospede.leitoNumero}}</span></span> 
        </div>                      
        <div>Status: <span class="fw-bold">{{hospede.statusHospedagem}}</span>
        
            <el-tooltip content="Ver Detalhes" placement="bottom" :open-delay="toolTipDelay">
                <el-button type="primary" plain size="mini" circle @click="onShowHospedagemInfo(hospede.hospedagemId)">
                <i class="fas fa-info"></i>
                </el-button>
            </el-tooltip>

        </div>                      
    </el-card>

</template>

<script>

export default {
    name: "CardHospede",

    components: {
        CardHospede
    },

    props: {
        hospede: {
            type: Object,
            default: {
                pessoaId: 0,
                hospedagemId: 0,
                nome: 'Unknow',
                cidadeUf: 'Unknow',
                fone: '9999-9999',
                statusHospedagem: 'Unknow',
                tipoUtilizacaoDescricao: 'Unknow',
                tipoUtilizacao: 'T',
                quartoNumero: 0,
                leitoNumero: 0,
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
        margin: 10px;
        width: 350px; 
        line-height: 1.5em;
    }

    .fw-bold{
        font-weight: bold;        
    }

    .titulo{
        font-size: 1.1em;
    }

</style>