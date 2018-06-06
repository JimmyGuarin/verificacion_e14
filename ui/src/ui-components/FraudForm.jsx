import React, {Component} from 'react';
import SelectCandidate from './SelectCandidate';
import CandidatesSelected from './CandidatesSelected';
import { getCandidates } from "../webapi/endpoints";

export default class FraudForm extends Component {
  constructor(props) {
    super(props);
    this.setAdded = this.setAdded.bind(this);
    this.sendData = this.sendData.bind(this);
    this.candidatesAdded = [];
    this.state = {
      added: 0,
      loading: true
    }
  }

  setAdded(candidateId, candidateName, votesNumber) {
    console.log("setAdded");
    this.candidatesAdded.push({
      candidatoId: parseInt(candidateId, 10),
      nombre: candidateName,
      votosSospechosos: parseInt(votesNumber, 10)
    });
    this.candidates = this.candidates.filter(s => s.id !== candidateId);
    this.setState({added: this.state.added + 1});
  }

  fetchCandidates = () => {  
    getCandidates().then(res => {
      this.candidates  = res.response;
      this.setState({loading: false});
    });
  }

  sendData(captchaValue) {
    const { handleSendFraud } = this.props;
    handleSendFraud(this.candidatesAdded, captchaValue);
  }

  render() {
    const { handleCancelFraud } = this.props;
    return (
      this.state.loading ?
      <div>
        <h1>CARGANDO...</h1>
      </div>
      :
      <div>
        <h4>Registro de E14 sospechoso</h4>
        <div className="fraude-form">
          <br/>
          <SelectCandidate 
            candidates={this.candidates}
            onAdded={this.setAdded}
          />
          <CandidatesSelected 
            candidatesAdded={this.candidatesAdded}
            onSend={this.sendData}
            onCancel={handleCancelFraud}
          />
        </div>
      </div>
    );
  }
  componentDidMount() {
    this.fetchCandidates();
  }
}