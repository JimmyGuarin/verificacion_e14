import React, {Component} from 'react';
import { Navbar, Nav, NavItem, Badge, Tooltip, OverlayTrigger } from 'react-bootstrap';

export default class NavigationBar extends Component {
  
  render() {
    const { handleLogout, user } = this.props;
    const tooltip = (text) => (
      <Tooltip id="tooltip">
        <strong>{text}</strong>
      </Tooltip>
    );
    return (
      <Navbar inverse collapseOnSelect>
        <Navbar.Header>
            <Navbar.Brand>
            <a>Verificación E14</a>
            </Navbar.Brand>
            <Navbar.Toggle />
        </Navbar.Header>
        <Navbar.Collapse>
            <Nav>
            <NavItem eventKey={1} href="/validar">
                Verificar
            </NavItem>
            <NavItem eventKey={2} href="/datos">
                Datos
            </NavItem>
            </Nav>
            <Nav pullRight>
              <NavItem eventKey={2} onClick={handleLogout}  href="#">
                Salir
              </NavItem>
            </Nav>
            <Nav pullRight>
              <Navbar.Text>
                  Reportados por {user.name}
              </Navbar.Text>
              <Navbar.Text>
                <OverlayTrigger placement="bottom" overlay={tooltip("Total reportados")}>
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

}















