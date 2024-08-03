import { useAppSelector } from "../redux/hooks";

const useAuth = () => {
    const user = useAppSelector((state) => state.auth.user);
    console.log("authority", user.authorities[0]);
    return {
        user,
        isAuthenticated: user!== null && user !== undefined,
        isAdmin: user?.authorities[0]['authority'] === 'ROLE_ADMIN',
    };
};

export default useAuth;
