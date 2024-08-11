import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { fetchUserList, deleteUser, activateUser, unblockUser } from '../../features/userSlice';
import { Button, Table, Spinner, Alert } from 'react-bootstrap';
import moment from 'moment';

const UserList = () => {
    const dispatch = useDispatch();
    const { userList, status, error } = useSelector(state => state.user);

    useEffect(() => {
        dispatch(fetchUserList());
    }, [dispatch]);

    const handleDelete = (userId) => {
        if (window.confirm('Are you sure you want to delete this user?')) {
            dispatch(deleteUser({ userId }));
        }
    };

    const handleActivate = (email) => {
        dispatch(activateUser({ email }));
    };

    const handleUnblock = (email) => {
        dispatch(unblockUser({ email }));
    };

    if (status === 'loading') {
        return <Spinner animation="border" />;
    }

    if (status === 'failed') {
        return <Alert variant="danger">Error: {error}</Alert>;
    }
    const users = Array.isArray(userList.users) ? userList.users : [];

    return (
        <div>
            <h1>User List</h1>
            {userList.length === 0 ? (
                <p>No users found.</p>
            ) : (
                <Table striped bordered hover>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Username</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>Email</th>
                            <th>Phone</th>
                            <th>Created At</th>
                            <th>Updated At</th>
                            <th>Status</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody>
                        {userList.map(user => (
                            <tr key={user.id}>
                                <td>{user.id}</td>
                                <td>{user.username}</td>
                                <td>{user.firstName}</td>
                                <td>{user.lastName}</td>
                                <td>{user.email}</td>
                                <td>{user.phone}</td>
                                <td>{moment(user.createdAt).format('YYYY-MM-DD HH:mm:ss')}</td>
                                <td>{moment(user.updatedAt).format('YYYY-MM-DD HH:mm:ss')}</td>
                                <td>
                                    {user.accountLocked ? 'Locked' : 'Active'}<br />
                                    {user.isDeleted ? 'Deleted' : 'Not Deleted'}
                                </td>
                                <td>
                                    <Button
                                        variant="success"
                                        onClick={() => handleActivate(user.email)}
                                        disabled={!user.accountLocked || user.isDeleted}
                                    >
                                        Activate
                                    </Button>
                                    <Button
                                        variant="warning"
                                        onClick={() => handleUnblock(user.email)}
                                        disabled={!user.accountLocked || user.isDeleted}
                                    >
                                        Unblock
                                    </Button>
                                    <Button
                                        variant="danger"
                                        onClick={() => handleDelete(user.id)}
                                        disabled={user.isDeleted}
                                    >
                                        Delete
                                    </Button>
                                </td>
                            </tr>
                        ))}
                    </tbody>
                </Table>
            )}
        </div>
    );
};

export default UserList;
