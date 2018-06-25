import React, {Component} from 'react';
import { Row, Table, Button, FormGroup, ControlLabel, FormControl } from 'react-bootstrap';
import Papa from 'papaparse';

export default class E14ReportedTable extends Component {
  constructor(props) {
    super(props);
    this.state= { 
      rowsTable: null,
      filterValue: -1
    };
    this.openExternal = this.openExternal.bind(this);
    this.applyFilter = this.applyFilter.bind(this);
    this.exportCsv = this.exportCsv.bind(this);
  }

  applyFilter(e) {
    const { resumen } = this.props;
    const value = e.target.value;

    if (value >= 0 ) {
      this.setState({rowsTable: [resumen[value]], filterValue: value});
    } else {
      this.setState({rowsTable: resumen, filterValue: value});
    }
  }
 
  openExternal(link) {
    window.open(link, "", "width=500,height=500");
  }

  exportCsv() {
    const { resumen } = this.props;
    let data = [];
    resumen.forEach(c => { 
      c.e14Reportes.forEach(e => {
        data.push({
          candidato: c.candidato.nombre,
          departamento: e.e14.departamento,
          municipio: e.e14.municipio,
          zona: e.e14.zona,
          puesto: e.e14.puesto, 
          "numer reportes": e.cantReportes,
          "votos reportados":e.votosReportados,
          link: e.e14.link
        })
      });
    });
    var csv = Papa.unparse(data);
    var blob = new Blob([csv]);
    var a = window.document.createElement("a");
    a.href = window.URL.createObjectURL(blob, {type: "text/plain"});
    a.download = "estadisticasE14.csv";
    document.body.appendChild(a);
    a.click();
    document.body.removeChild(a);    
}

  render() {
    const { rowsTable, filterValue } = this.state;
    const { resumen } = this.props;
    
    const candidatesFilter = resumen.map((c, i) =>{
      return <option value={i}>{c.candidato.nombre}</option>
    });
    const rows = rowsTable.map((c, index) => { 
      return c.e14Reportes.map((e, i) => {
        return (<tr>
            <td>{c.candidato.nombre}</td>
           <td>{e.e14.departamento}</td>
           <td>{e.e14.municipio}</td>
           <td>{e.e14.zona}</td>
           <td>{e.e14.puesto}</td>
           <td>{e.cantReportes}</td>
           <td>{e.votosReportados}</td>
           <td><Button bsStyle="link" onClick={() => this.openExternal(e.link)}>Link</Button></td>
         </tr>);
      });
    });
    
    return (
      <div>
        <Row>
          <h3 align="center"> Información E14 Reportados</h3>    
        </Row>
        <div>
        <FormGroup controlId="formControlsSelect">
          <ControlLabel>Filtrar por candidato</ControlLabel>
          <FormControl onChange={this.applyFilter} componentClass="select" placeholder="select" value={filterValue}>
          <option value="-1">Todos</option>
          {candidatesFilter}
          </FormControl>
        </FormGroup>
        </div>
      <Table bordered condensed>
        <thead className="thead-dark">
          <tr>
            <th valign="center">Candidato</th>
            <th valign="center">Departamento</th>
            <th valign="center">Municipio</th>
            <th valign="center">Zona</th>
            <th valign="center">Puesto</th>
            <th valign="center">Cantidad reportes</th>
            <th valign="center">Votos reportados</th>
            <th valign="center">Link</th>
          </tr>
        </thead>
        <tbody>
          { rows }
        </tbody>
      </Table>
      <div>
        <Button bsStyle="primary" onClick={this.exportCsv}>Exportar</Button>    
      </div>
      </div>
    );     
  }

  componentWillMount() { 
    const { resumen } = this.props;
    this.setState({rowsTable: resumen});
  }

}