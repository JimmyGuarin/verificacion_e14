import React, {Component} from 'react';
import { Navbar, Nav, NavItem, Badge, Tooltip, OverlayTrigger } from 'react-bootstrap';

export default class NavigationBar extends Component {
  
  render() {
    const { handleLogout, showHelpModal, user } = this.props;
    const tooltip = (text) => (
      <Tooltip id="tooltip">
        <strong>{text}</strong>
      </Tooltip>
    );
    return (
      <Navbar inverse collapseOnSelect>
        <Navbar.Header>
            <Navbar.Brand>
              <a>Transparencia electoral</a>
            </Navbar.Brand>
            <Navbar.Brand className="hidden-md hidden-lg">
              <NavItem style={{display : 'block'}}>
                <Badge className="badge-success">{user.reportes}</Badge>
              </NavItem>
            </Navbar.Brand>
            <Navbar.Brand className="hidden-md hidden-lg">
              <NavItem style={{display : 'block'}}>
                <Badge className="badge-error">{user.sospechosos}</Badge>
              </NavItem>
            </Navbar.Brand>
            <Navbar.Toggle/>
        </Navbar.Header>
        <Navbar.Collapse>
            <Nav>
            <NavItem eventKey={1} href="/validar">
                Verificar E14
            </NavItem>
            <NavItem eventKey={2} href="/estadisticas">
                Estadísticas 
            </NavItem>
            <NavItem eventKey={2} onClick={showHelpModal} >
                Ayuda 
            </NavItem>
            <NavItem eventKey={2} href="/quienesomos">
              ¿Quiénes somos?
            </NavItem>
            </Nav>
            <Nav pullRight>
              <NavItem eventKey={2} onClick={handleLogout}  href="#">
                Salir
              </NavItem>
            </Nav>
            <Nav pullRight>
              <Navbar.Text>
                   E14 verificados por {user.name}
              </Navbar.Text>
              <Navbar.Text>
                <OverlayTrigger placement="bottom" overlay={tooltip("Total verificados")}>
                  <Badge className="badge-success">{user.reportes}</Badge>
                 </OverlayTrigger>
              </Navbar.Text>
              <Navbar.Text>
                <OverlayTrigger placement="bottom" overlay={tooltip("Total Sospechosos")}>
                  <Badge className="badge-error">{user.sospechosos}</Badge>
                </OverlayTrigger>
              </Navbar.Text>
            </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }

  componentDidMount() {
    console.log("componentDidMount navigation");
  
  }

}















