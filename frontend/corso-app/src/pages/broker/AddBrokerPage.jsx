import React, { useState } from 'react';
import { Form, Button, Container, Alert, Spinner } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css'; // Bootstrap stil dosyasını import et
import { registerBroker } from '../../features/userSlice'; // Redux eylemi
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
    const [loading, setLoading] = useState(false); // Yükleme durumu ekle

    const handleChange = (e) => {
        setBroker({ ...broker, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true); // Form gönderildiğinde yüklemeyi başlat
        try {
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
            setError('Broker eklenirken bir hata oluştu.');
            setSuccess('');
        } finally {
            setLoading(false); // Yüklemeyi bitir
        }
    };

    return (
        <Container className="my-5" style={{ maxWidth: '600px' }}>
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

                <Form.Group controlId="formLastName" className="mt-3">
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

                <Form.Group controlId="formUsername" className="mt-3">
                    <Form.Label>Kullanıcı Adı</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Kullanıcı adınızı girin"
                        name="username"
                        value={broker.username}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formEmail" className="mt-3">
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

                <Form.Group controlId="formPhone" className="mt-3">
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

                <Button 
                    variant="primary" 
                    type="submit" 
                    className="mt-4 w-100"
                    disabled={loading} // Yükleme durumunda butonu devre dışı bırak
                >
                    {loading ? <Spinner animation="border" size="sm" /> : 'Ekle'}
                </Button>
            </Form>
        </Container>
    );
};

export default AddBrokerPage;
