import React, {Component} from 'react';
import { Row, Col, Button, FormGroup, Form, 
    ControlLabel, FormControl, HelpBlock  } from 'react-bootstrap';

export default class SelectCandidate extends Component { 
  
  constructor(props) {
    super(props);
    this.handleChangeSelect = this.handleChangeSelect.bind(this);
    this.handleChangeVotes = this.handleChangeVotes.bind(this);
    this.sendSelected = this.sendSelected.bind(this);
    this.candidateOptions = props.candidates;
    this.state = {
      selectValue: "",
      selectName: "",
      voteValue: 0,
    };
  }

  sendSelected(e) {
    const { onAdded } = this.props;
    console.log("sendSelected");
    if(this.state.selectValue > 0 && this.state.voteValue>0)
      onAdded(this.state.selectValue, this.state.selectName, this.state.voteValue); 
    else
      alert("Valores Invalidos");
  }

  getValidationSelect() {
    const option = this.state.selectValue;
    if (option > 0) return 'success'; 
    else return 'error';
    return null;
  }

  getValidationVote() {
    const option = this.state.voteValue;
    if (option > 0) return 'success'; 
    else return 'error';
    return null;
  }

  handleChangeSelect(e) {
    this.setState({ 
      selectValue: e.target.value,
      selectName: this.candidateOptions[e.target.selectedIndex - 1].nombre
    });
  }

  handleChangeVotes(e) {
    this.setState({ voteValue: e.target.value });    
  }

  render() {
    return(  
    <Form horizontal>
      <FormGroup controlId="formHorizontalEmail" validationState={this.getValidationSelect()}>
        <Col componentClass={ControlLabel} sm={3}>
            Candidato
        </Col>
        <Col sm={6}>
            <FormControl 
              componentClass="select" 
              placeholder="select"
              value={this.state.selectValue}
              data-nombre="select"
              onChange={this.handleChangeSelect}>
              <option value="">Select...</option>
              {
                this.candidateOptions.map( c => {
                  return (<option value={c.id} data-nombre={c.nombre}>{c.nombre}</option>)
                })
              }
            </FormControl>
        </Col>
        </FormGroup>

        <FormGroup 
          controlId="formHorizontalPassword"
          validationState={this.getValidationVote()}> 
        <Col componentClass={ControlLabel} sm={3}>
            Votos Sospechosos
        </Col>
        <Col sm={6}>
            <FormControl 
              type="number"
              value={this.state.voteValue}  
              onChange={this.handleChangeVotes}
              placeholder="100"/> 
        </Col>
        <Col sm={3}>
            <Button bsStyle="success"  onClick={this.sendSelected}>
              Agregar
            </Button>
        </Col>          
      </FormGroup>`
    </Form>);
  }

  componentWillReceiveProps(nextProps) {
    console.log("componentWillReceiveProps");
    this.candidateOptions = nextProps.candidates;
    this.setState({
        selectValue: "",
        selectName: "",
        voteValue: 0,
    });
  }

}