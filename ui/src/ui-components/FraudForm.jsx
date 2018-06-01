import React, {Component} from 'react';
import SelectCandidate from './SelectCandidate';
import CandidatesSelected from './CandidatesSelected';



export default class FraudForm extends Component {
  constructor(props) {
    super(props);
    this.setAdded = this.setAdded.bind(this);
    this.sendData = this.sendData.bind(this);
    this.candidates = [
      {
        id: 1,
        name: "Iván Duque"
      },
      {
        id: 2,
        name: "Gustavo Petro"
      }
    ]
    this.candidatesAdded = [];
    this.state = {
      added: 0
    }
  }

  setAdded(candidateId, candidateName, votesNumber) {
    console.log("setAdded");
    this.candidatesAdded.push({
      id: candidateId,
      name: candidateName,
      votes: votesNumber
    });
    this.candidates = this.candidates.filter(s => s.id != candidateId);
    this.setState({added: this.state.added + 1});
  }

  sendData() {
    const { handleSendFraud } = this.props;
    //TODO CONSTRUIR EL JSON
  }

  render() {
    const { handleCancelFraud } = this.props;
    return (
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
}