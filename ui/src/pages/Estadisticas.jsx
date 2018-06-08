import React, {Component} from 'react';
import { Row, Col } from 'react-bootstrap';
import { getResumen } from '../webapi/endpoints';
import { Chart } from 'react-google-charts';

export default class Estadisticas extends Component {
  constructor(props) {
    super(props);
    this.chartEvents = [
        {
          eventName: 'select',
          callback(Chart) {
              // Returns Chart so you can access props and  the ChartWrapper object from chart.wrapper
            console.log('Selected ', Chart.chart.getSelection());
          },
        },
      ];

    this.state = {
      options: {
        title: 'Candidatos vs. Votos sospechosos',
        hAxis: { title: 'Votos sospechosos'},
        vAxis: { title: 'Candidatos'},
        legend: 'none',
      },
      resumen: null,
      totalE14Revisados: 0,
      totalRevisiones: 0
    };
  }
 

  render() {
  const { resumen, options, totalE14Revisados, totalRevisiones } = this.state;
  return (<div className="container-estadisticas">
    <Row>
      <Col md={12}>
        <h1 align="center">Estadísticas</h1>
        <hr/>
      </Col>
    </Row>
    <Row>
     <Col md={6}>
     {
        resumen ?
          <Row>
          <Col md={12}>
            <div style={{height: '500px'}}>
            <Chart
                chartType="BarChart"
                data={[['Age', 'Votos sospechosos', { role: 'style' }], ...resumen ]}
                options={options}
                graph_id="BarChart"
                width="100%"
                height={'90%'}
                legend_toggle
                chartEvents={this.chartEvents}
            /> 
            </div>
            </Col>
            <Col md={12} style={{marginTop:'-10%'}}>
             <h5 align="center">Valores tomados de <span style={{fontWeight: 'bold'}}>{totalRevisiones}</span> revisiones en un número de <span style={{fontWeight: 'bold'}}>{totalE14Revisados}</span> formatos e14</h5>
            </Col>
        </Row>
          : null
      }
     </Col>
    </Row>
    <br/>
    <Row>
    </Row>
    <br/>
    <Row>
      <Col xsOffset={4} mdOffset={5} xs={4} md={2} >
      </Col>
    </Row>
    <br/>
    </div>
    );
  }

  componentWillMount() { 
    const defaultColors = [
      'gray',
      'green',
      'blue',
      'red',
      'yellow',
      'orange',
      'blueviolet',
      'brown',
      'darkblue'
    ]  
    getResumen().then(res => {
      let totalE14Revisados = 0;
      let totalRevisiones = 0; 
      const resumen = res.response.resumen.map((c, index) => {
        totalE14Revisados+= c.e14Reportes.length;
        totalRevisiones+= c.cantReportes;
        return [c.candidato.nombre, c.votos, 'color:' + defaultColors[index]]});
      this.setState({resumen, totalE14Revisados, totalRevisiones}); 
    });
  }
}