import React, {Component} from 'react';
import { Row, Col } from 'react-bootstrap';
import { getResumen } from '../webapi/endpoints';
import BarChart from '../ui-components/BarChart';
import E14ReportedTable from '../ui-components/E14ReportedTable';


export default class Estadisticas extends Component {
  constructor(props) {
    super(props);
    this.state = {
      resumen: null
    };
  }
 

  render() {
    const { resumen } = this.state;
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
       <BarChart resumen={resumen}/>
       : null
     }
     </Col>
     <Col md={6}>
     { 
       resumen ?
       <E14ReportedTable resumen={resumen}/>
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
    getResumen().then(res => {
      this.setState({resumen: res.response.resumen});
    });
  }
}