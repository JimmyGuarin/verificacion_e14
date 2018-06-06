import React, {Component} from 'react';
import { Navbar, Nav, NavItem } from 'react-bootstrap';

export default class NavigationBar extends Component {
  
  render() {
    const { handleLogout, userName } = this.props;
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
              <Navbar.Text>
                Bienvenido: {userName}
              </Navbar.Text>
              <NavItem eventKey={2} onClick={handleLogout}  href="#">
                Salir
              </NavItem>
            </Nav>
        </Navbar.Collapse>
      </Navbar>
    );
  }

}















