import React, { useState } from 'react';
import { Form, Button, Container, Alert } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css'; // Bootstrap stil dosyasını import et
import {registerBroker} from '../../features/userSlice'; // Redux eylemi
import { useDispatch } from 'react-redux';

const AddBrokerPage = () => {
        const dispatch = useDispatch();
        const [broker, setBroker] = useState({
        firstName: '',
        lastName: '',
        username: '',
        email: '',
        phone: '',

    });

    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');

    const handleChange = (e) => {
        setBroker({ ...broker, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            console.log(broker);
            await dispatch(registerBroker(broker)).unwrap();
            setSuccess('Broker başarıyla eklendi.');
            setError('');
            setBroker({
                firstName: '',
                lastName: '',
                username: '',
                email: '',
                phone: '',

            });
        } catch (err) {
            setError('Broker asdas asdasd hata oluştu.');
            setSuccess('');
          
        }
    };

    return (
        <Container style={{ maxWidth: '600px', marginTop: '50px' }}>
            <h1 className="text-center mb-4">Personel Ekle</h1>
            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="formFirstName">
                    <Form.Label>İsim</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="İsminizi girin"
                        name="firstName"
                        value={broker.firstName}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formLastName">
                    <Form.Label>Soyisim</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Soyisminizi girin"
                        name="lastName"
                        value={broker.lastName}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formUsername">
                    <Form.Label>username</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="username giriniz"
                        name="username"
                        value={broker.username}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formEmail">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                        type="email"
                        placeholder="Email adresinizi girin"
                        name="email"
                        value={broker.email}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formPhone">
                    <Form.Label>Telefon Numarası</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Telefon numaranızı girin"
                        name="phone"
                        value={broker.phone}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Button variant="primary" type="submit" className="mt-3">
                    Ekle
                </Button >
            </Form>
        </Container>
    );
};

export default AddBrokerPage;
