import React, { useState } from 'react';
import { Form, Button, Container, Alert, Spinner } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import { useDispatch } from 'react-redux';
import { changePassword } from '../../features/userSlice';

const ChangePassword = () => {
    const dispatch = useDispatch();
    const [formData, setFormData] = useState({
        currentPassword: '',
        newPassword: '',
        confirmNewPassword: '',
    });

    const [error, setError] = useState('');
    const [success, setSuccess] = useState('');
    const [loading, setLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData({
            ...formData,
            [name]: value,
        });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (formData.newPassword !== formData.confirmNewPassword) {
            setError('Yeni şifreler eşleşmiyor.');
            setSuccess('');
            return;
        }
        setLoading(true);
        try {
            await dispatch(changePassword({
                oldPassword: formData.currentPassword,
                newPassword: formData.newPassword,
            })).unwrap();
            setSuccess('Şifre başarıyla değiştirildi.');
            setError('');
            setFormData({
                currentPassword: '',
                newPassword: '',
                confirmNewPassword: '',
            });
        } catch (err) {
            setError('Şifre değiştirilirken bir hata oluştu.');
            setSuccess('');
        } finally {
            setLoading(false);
        }
    };

    return (
        <Container className="my-5" style={{ maxWidth: '600px' }}>
            <h1 className="text-center mb-4">Şifre Değiştir</h1>
            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}
            <Form onSubmit={handleSubmit}>
                <Form.Group controlId="formCurrentPassword">
                    <Form.Label>Mevcut Şifre</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Mevcut şifrenizi girin"
                        name="currentPassword"
                        value={formData.currentPassword}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formNewPassword" className="mt-3">
                    <Form.Label>Yeni Şifre</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Yeni şifrenizi girin"
                        name="newPassword"
                        value={formData.newPassword}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Form.Group controlId="formConfirmNewPassword" className="mt-3">
                    <Form.Label>Yeni Şifreyi Onayla</Form.Label>
                    <Form.Control
                        type="password"
                        placeholder="Yeni şifrenizi tekrar girin"
                        name="confirmNewPassword"
                        value={formData.confirmNewPassword}
                        onChange={handleChange}
                        required
                    />
                </Form.Group>

                <Button 
                    variant="primary" 
                    type="submit" 
                    className="mt-4 w-100"
                    disabled={loading}
                >
                    {loading ? <Spinner animation="border" size="sm" /> : 'Şifreyi Değiştir'}
                </Button>
            </Form>
        </Container>
    );
};

export default ChangePassword;
