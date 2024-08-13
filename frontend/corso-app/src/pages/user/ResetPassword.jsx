import React, { useState } from "react";
import { Container, Form, Button, Alert } from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css'; // Bootstrap stil dosyasını import et
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";

function SetPassword() {
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const navigate = useNavigate();
    const location = useLocation();

    const handlePasswordChange = async (e) => {
        e.preventDefault();

        if (password !== confirmPassword) {
            setError("Şifreler uyuşmuyor.");
            return;
        }

        const query = new URLSearchParams(location.search);
        const token = query.get("token");

        if (!token) {
            setError("Geçersiz token.");
            return;
        }

        try {
            const response = await axios.put(
                `http://localhost:8080/api/v1/user/set-password?token=${encodeURIComponent(token)}`, 
                {
                    password: password
                },
                {
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }
            );

            setSuccess(response.data.message);
            setError(""); // Hata mesajını sıfırla
            navigate("/login"); // Başarılı işlemden sonra kullanıcıyı giriş sayfasına yönlendir

        } catch (error) {
            setError("Şifre oluşturulurken bir hata oluştu: " + (error.response?.data?.message || error.message));
            setSuccess("");
        }
    };

    return (
        <Container
            className="d-flex align-items-center justify-content-center"
            style={{ minHeight: '100vh' }}
        >
            <div className="p-4 border rounded shadow-sm bg-light" style={{ width: '100%', maxWidth: '400px' }}>
                <h2 className="text-center mb-4">Yeni Şifre Belirle</h2>
                {error && <Alert variant="danger">{error}</Alert>}
                {success && <Alert variant="success">{success}</Alert>}
                <Form onSubmit={handlePasswordChange}>
                    <Form.Group controlId="formNewPassword">
                        <Form.Label>Yeni Şifre</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Yeni şifrenizi girin"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Form.Group controlId="formConfirmPassword">
                        <Form.Label>Yeni Şifre Onayı</Form.Label>
                        <Form.Control
                            type="password"
                            placeholder="Yeni şifrenizi tekrar girin"
                            value={confirmPassword}
                            onChange={(e) => setConfirmPassword(e.target.value)}
                            required
                        />
                    </Form.Group>

                    <Button variant="primary" type="submit" className="mt-3 w-100">
                        Şifreyi Güncelle
                    </Button>
                </Form>
            </div>
        </Container>
    );
}

export default SetPassword;
