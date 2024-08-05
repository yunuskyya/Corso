import { useAppSelector } from "../redux/hooks";

const useAuth = () => {
    const user = useAppSelector((state) => state.auth.user);
    const fetch_status = useAppSelector((state) => state.auth.fetch_status);
    const status = useAppSelector((state) => state.auth.status);

    console.log("authority", user? user.authorities[0] : "no user");
    return {
        user,
        status,
        fetch_status,
        isAuthenticated: user!== null && user !== undefined,
        isAdmin: user?.authorities[0]['authority'] === 'ROLE_ADMIN',
    };
};

export default useAuth;
