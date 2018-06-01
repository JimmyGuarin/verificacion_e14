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
      candidatoId: parseInt(candidateId),
      nombre: candidateName,
      votosSospechosos: parseInt(votesNumber)
    });
    this.candidates = this.candidates.filter(s => s.id != candidateId);
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
        <SelectCandidate 
          candidates={this.candidates}
          onAdded={this.setAdded}
        />
        <hr/>
        <CandidatesSelected 
          candidatesAdded={this.candidatesAdded}
          onSend={this.sendData}
          onCancel={handleCancelFraud}
        />
      </div>
    );
  }
  componentDidMount() {
    this.fetchCandidates();
  }
}