var moment = require('moment')
moment.locale('pt-br');

export default {

    formatDate(dia, formato) {
      formato = formato || "DD/MM/YYYY"
      return dia ? moment(dia).format(formato) : ""
    },

    diaSemana(dia){
      return moment(dia).format("ddd")
    },
  
    getIndiceData(data){
      return moment(data).format("e")
    },

    hoje(){
      return moment().format("YYYY-MM-DD")
    },

    semanaAnterior(data){
      return moment(data).add(-7, "days").format("YYYY-MM-DD")
    },

    semanaSeguinte(data){
      return moment(data).add(1, "days").format("YYYY-MM-DD")
    },
  
}

/* 
exemplosMoment(){

  https://tableless.com.br/trabalhando-com-moment/

  const dia = moment("2018-25-02")
  moment("abcxyz").isValid() // false
  moment("2018-02-24").add(2, "days") // 2018-02-26
  moment("2018-02-24").add(1, "year").subtract("1", "days") // 2019-02-23

  moment().format("dd/MM/yyyy HH-mm") // 25/02/2018 13-35
  moment("abcxyz").format('YYYY MM DD') // "Invalid date"

  moment('2017-10-20').isBefore('2017-10-21'); // true
  moment('2017-10-20').isBefore('2010-12-31', 'year'); // false
  moment('2017-10-20').isBefore('2018-01-01', 'year'); // true

  moment('2010-10-20').isBetween('2010-10-19', '2010-10-25'); // true
}
*/
