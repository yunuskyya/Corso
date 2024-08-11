import { Button, Row } from "react-bootstrap";


export const Unauthorized = () => {
    return (
        <div className="d-flex justify-content-center align-items-center" style={{ height: '100vh' }}>
            <div class="alert alert-warning" role="alert">
                <h3>Bu sayfayı görüntülemek için yetkiniz yok, giriş yapılmalı!</h3>
                <Row className="justify-content-center mt-4">
                    <Button variant="secondary" href="/login" className="col-4">Giriş Yap</Button>
                </Row>
            </div>
        </div>
    );
}