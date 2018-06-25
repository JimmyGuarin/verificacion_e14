import React, {Component} from 'react';
import { Row, Col } from 'react-bootstrap';
import { Chart } from 'react-google-charts';

export default class BarChart extends Component {
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
    this.options = {
      title: 'Candidatos vs. Votos sospechosos',
      hAxis: { title: 'Votos sospechosos'},
      vAxis: { title: 'Candidatos'},
      legend: 'none',
    }
    this.totalE14Revisados = 0;
    this.totalRevisiones = 0;
  }
 

  render() {
    return (
      <Row>
        <Col md={12}>
            <div style={{height: '500px'}}>
            <Chart
                chartType="BarChart"
                data={[['Age', 'Votos sospechosos', { role: 'style' }], ...this.resumen ]}
                options={this.options}
                graph_id="BarChart"
                width="100%"
                height={'90%'}
                legend_toggle
                chartEvents={this.chartEvents}
            /> 
            </div>
        </Col>
        <Col md={12}>
            <h5 align="center">Valores tomados de <span style={{fontWeight: 'bold'}}>{this.totalRevisiones}</span> revisiones en un total de <span style={{fontWeight: 'bold'}}>{this.totalE14Revisados}</span> formatos e14 con anomalías</h5>
        </Col>
        </Row>
    );     
  }

  componentWillMount() { 
    const resumen = this.props.resumen;
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
    this.resumen = resumen.map((c, index) => {
      this.totalE14Revisados+= c.e14Reportes.length;
      this.totalRevisiones+= c.cantReportes;
      return [c.candidato.nombre, c.votos, 'color:' + defaultColors[index]]});
  }
}