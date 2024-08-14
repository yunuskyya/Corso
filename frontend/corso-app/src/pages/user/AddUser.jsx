import React, { useState } from 'react';
import { Form, Button, Container, Alert, Spinner } from 'react-bootstrap';
import { useDispatch } from 'react-redux';
import { registerManager } from '../../features/userSlice'; 

const AddUser = () => {
    const dispatch = useDispatch();
    const [formData, setFormData] = useState({
        firstName: '',
        phone: '',
        lastName: '',
        email: '',
        password: '',
    });

    const [error, setError] = useState({});
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handlePhoneChange = (e) => {
        const { name, value } = e.target;
        const filteredValue = value.replace(/[^0-9]/g, ''); 
        setFormData({
            ...formData,
            [name]: filteredValue,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setLoading(true); 
        try {
            await dispatch(registerManager(formData)).unwrap();
            setSuccess('Yönetici başarıyla oluşturuldu.');
            setError({});

            setTimeout(() => {
                setSuccess('');
            }, 3000);

            setFormData({
                firstName: '',
                phone: '',
                lastName: '',
                email: '',
                password: '',
            });
        } catch (err) {
            if (err.response && err.response.data) {
                setError(err.response.data); // Backend'den gelen hata mesajları
            } else {
                setError({ global: 'Yönetici oluşturulurken bir hata oluştu.' });
            }
            setSuccess('');

            setTimeout(() => {
                setError({});
            }, 3000);
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container style={{ maxWidth: '600px', marginTop: '50px' }}>
            <h1 className="text-center mb-4">Yönetici Oluştur</h1>
            {error.global && <Alert variant="danger">{error.global}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="formFirstName">
                    <Form.Label>İsim</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="İsminizi girin"
                        name="firstName"
                        value={formData.firstName}
                        onChange={handleChange}
                        isInvalid={!!error.firstName} // Hata varsa kırmızı yap
                    />
                    <Form.Control.Feedback type="invalid">
                        {error.firstName}
                    </Form.Control.Feedback>
                </Form.Group>

                <Form.Group controlId="formLastName">
                    <Form.Label>Soyisim</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Soyisminizi girin"
                        name="lastName"
                        value={formData.lastName}
                        onChange={handleChange}
                        isInvalid={!!error.lastName}
                    />
                    <Form.Control.Feedback type="invalid">
                        {error.lastName}
                    </Form.Control.Feedback>
                </Form.Group>

                <Form.Group controlId="formPhone">
                    <Form.Label>Telefon Numarası</Form.Label>
                    <Form.Control
                        type="text"
                        placeholder="Telefon numaranızı girin"
                        name="phone"
                        value={formData.phone}
                        onChange={handlePhoneChange}
                        isInvalid={!!error.phone}
                    />
                    <Form.Control.Feedback type="invalid">
                        {error.phone}
                    </Form.Control.Feedback>
                </Form.Group>

                <Form.Group controlId="formEmail">
                    <Form.Label>Email</Form.Label>
                    <Form.Control
                        type="email"
                        placeholder="Email adresinizi girin"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        isInvalid={!!error.email}
                    />
                    <Form.Control.Feedback type="invalid">
                        {error.email}
                    </Form.Control.Feedback>
                </Form.Group>

                <Form.Group controlId="formPassword">
                    <Form.Label>Şifre</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Şifrenizi girin"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        isInvalid={!!error.password}
                    />
                    <Form.Control.Feedback type="invalid">
                        {error.password}
                    </Form.Control.Feedback>
                </Form.Group>

                <Button 
                    variant="primary" 
                    type="submit" 
                    className="mt-3" 
                    size="lg" 
                    style={{ width: '100%' }} 
                    disabled={loading}
                >
                    {loading ? (
                        <div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center' }}>
                            <Spinner
                                as="span"
                                animation="border"
                                size="lg"
                                role="status"
                                aria-hidden="true"
                                style={{ width: '2rem', height: '2rem' }}
                            /> 
                            <span style={{ marginLeft: '10px' }}>Yükleniyor...</span>
                        </div>
                    ) : (
                        'Oluştur'
                    )}
                </Button>
            </Form>
        </Container>
    );
};

export default AddUser;
