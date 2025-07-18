PGDMP               	        }           DBFINALE    17.4    17.4 0    b           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                           false            c           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                           false            d           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                           false            e           1262    24822    DBFINALE    DATABASE     p   CREATE DATABASE "DBFINALE" WITH TEMPLATE = template0 ENCODING = 'UTF8' LOCALE_PROVIDER = libc LOCALE = 'it-IT';
    DROP DATABASE "DBFINALE";
                     postgres    false            W           1247    24824 	   statovolo    TYPE     �   CREATE TYPE public.statovolo AS ENUM (
    'programmato',
    'inRitardo',
    'decollato',
    'atterrato',
    'cancellato'
);
    DROP TYPE public.statovolo;
       public               postgres    false            �            1259    24835    bagaglio    TABLE     �  CREATE TABLE public.bagaglio (
    codice character varying(50) NOT NULL,
    id_prenotazione integer,
    stato character varying(20) NOT NULL,
    CONSTRAINT chk_stato_bagaglio CHECK (((stato)::text = ANY (ARRAY[('smarrito'::character varying)::text, ('ritirabile'::character varying)::text, ('caricato'::character varying)::text, ('inElaborazione'::character varying)::text])))
);
    DROP TABLE public.bagaglio;
       public         heap r       postgres    false            �            1259    24839    bagaglio_codice_seq    SEQUENCE     �   CREATE SEQUENCE public.bagaglio_codice_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.bagaglio_codice_seq;
       public               postgres    false    217            f           0    0    bagaglio_codice_seq    SEQUENCE OWNED BY     K   ALTER SEQUENCE public.bagaglio_codice_seq OWNED BY public.bagaglio.codice;
          public               postgres    false    218            �            1259    24840    gate    TABLE     :   CREATE TABLE public.gate (
    numero integer NOT NULL
);
    DROP TABLE public.gate;
       public         heap r       postgres    false            �            1259    24843 
   passeggero    TABLE     �   CREATE TABLE public.passeggero (
    id_passeggero integer NOT NULL,
    nome character varying(50) NOT NULL,
    cognome character varying(50) NOT NULL,
    numero_documento character varying(30) NOT NULL
);
    DROP TABLE public.passeggero;
       public         heap r       postgres    false            �            1259    24846    passeggero_id_seq    SEQUENCE     �   CREATE SEQUENCE public.passeggero_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 (   DROP SEQUENCE public.passeggero_id_seq;
       public               postgres    false    220            g           0    0    passeggero_id_seq    SEQUENCE OWNED BY     R   ALTER SEQUENCE public.passeggero_id_seq OWNED BY public.passeggero.id_passeggero;
          public               postgres    false    221            �            1259    24847    posto    TABLE     �   CREATE TABLE public.posto (
    codice_volo character varying(10) NOT NULL,
    posto character varying(4) NOT NULL,
    occupato boolean DEFAULT false NOT NULL
);
    DROP TABLE public.posto;
       public         heap r       postgres    false            �            1259    24851    prenotazione    TABLE     0  CREATE TABLE public.prenotazione (
    id_prenotazione integer NOT NULL,
    codice_volo character varying(10),
    id_passeggero integer,
    numero_biglietto character varying(20) NOT NULL,
    posto character varying(4),
    stato character varying(20) NOT NULL,
    username character varying(16)
);
     DROP TABLE public.prenotazione;
       public         heap r       postgres    false            �            1259    24854    prenotazione_id_seq    SEQUENCE     �   CREATE SEQUENCE public.prenotazione_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 *   DROP SEQUENCE public.prenotazione_id_seq;
       public               postgres    false    223            h           0    0    prenotazione_id_seq    SEQUENCE OWNED BY     X   ALTER SEQUENCE public.prenotazione_id_seq OWNED BY public.prenotazione.id_prenotazione;
          public               postgres    false    224            �            1259    24855    utente    TABLE     �  CREATE TABLE public.utente (
    username character varying(16) NOT NULL,
    password character varying(100) NOT NULL,
    ruolo character varying(20) NOT NULL,
    nome character varying(50),
    cognome character varying(50),
    CONSTRAINT utente_ruolo_check CHECK (((ruolo)::text = ANY (ARRAY[('generico'::character varying)::text, ('amministratore'::character varying)::text])))
);
    DROP TABLE public.utente;
       public         heap r       postgres    false            �            1259    24859    volo    TABLE     �  CREATE TABLE public.volo (
    codice character varying(10) NOT NULL,
    compagnia character varying(100) NOT NULL,
    origine character varying(100) NOT NULL,
    destinazione character varying(100) NOT NULL,
    data date NOT NULL,
    orario time without time zone NOT NULL,
    posti_totali integer DEFAULT 0 NOT NULL,
    posti_disponibili integer DEFAULT 0 NOT NULL,
    ritardo integer,
    gate integer,
    stato public.statovolo
);
    DROP TABLE public.volo;
       public         heap r       postgres    false    855            �           2604    24864    bagaglio codice    DEFAULT     r   ALTER TABLE ONLY public.bagaglio ALTER COLUMN codice SET DEFAULT nextval('public.bagaglio_codice_seq'::regclass);
 >   ALTER TABLE public.bagaglio ALTER COLUMN codice DROP DEFAULT;
       public               postgres    false    218    217            �           2604    24865    passeggero id_passeggero    DEFAULT     y   ALTER TABLE ONLY public.passeggero ALTER COLUMN id_passeggero SET DEFAULT nextval('public.passeggero_id_seq'::regclass);
 G   ALTER TABLE public.passeggero ALTER COLUMN id_passeggero DROP DEFAULT;
       public               postgres    false    221    220            �           2604    24866    prenotazione id_prenotazione    DEFAULT        ALTER TABLE ONLY public.prenotazione ALTER COLUMN id_prenotazione SET DEFAULT nextval('public.prenotazione_id_seq'::regclass);
 K   ALTER TABLE public.prenotazione ALTER COLUMN id_prenotazione DROP DEFAULT;
       public               postgres    false    224    223            V          0    24835    bagaglio 
   TABLE DATA           B   COPY public.bagaglio (codice, id_prenotazione, stato) FROM stdin;
    public               postgres    false    217   �<       X          0    24840    gate 
   TABLE DATA           &   COPY public.gate (numero) FROM stdin;
    public               postgres    false    219   �?       Y          0    24843 
   passeggero 
   TABLE DATA           T   COPY public.passeggero (id_passeggero, nome, cognome, numero_documento) FROM stdin;
    public               postgres    false    220   �?       [          0    24847    posto 
   TABLE DATA           =   COPY public.posto (codice_volo, posto, occupato) FROM stdin;
    public               postgres    false    222   1G       \          0    24851    prenotazione 
   TABLE DATA           }   COPY public.prenotazione (id_prenotazione, codice_volo, id_passeggero, numero_biglietto, posto, stato, username) FROM stdin;
    public               postgres    false    223   3a       ^          0    24855    utente 
   TABLE DATA           J   COPY public.utente (username, password, ruolo, nome, cognome) FROM stdin;
    public               postgres    false    225   �f       _          0    24859    volo 
   TABLE DATA           �   COPY public.volo (codice, compagnia, origine, destinazione, data, orario, posti_totali, posti_disponibili, ritardo, gate, stato) FROM stdin;
    public               postgres    false    226   �g       i           0    0    bagaglio_codice_seq    SEQUENCE SET     B   SELECT pg_catalog.setval('public.bagaglio_codice_seq', 1, false);
          public               postgres    false    218            j           0    0    passeggero_id_seq    SEQUENCE SET     A   SELECT pg_catalog.setval('public.passeggero_id_seq', 143, true);
          public               postgres    false    221            k           0    0    prenotazione_id_seq    SEQUENCE SET     C   SELECT pg_catalog.setval('public.prenotazione_id_seq', 150, true);
          public               postgres    false    224            �           2606    24868    bagaglio bagaglio_pkey 
   CONSTRAINT     X   ALTER TABLE ONLY public.bagaglio
    ADD CONSTRAINT bagaglio_pkey PRIMARY KEY (codice);
 @   ALTER TABLE ONLY public.bagaglio DROP CONSTRAINT bagaglio_pkey;
       public                 postgres    false    217            �           2606    24870    gate gate_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.gate
    ADD CONSTRAINT gate_pkey PRIMARY KEY (numero);
 8   ALTER TABLE ONLY public.gate DROP CONSTRAINT gate_pkey;
       public                 postgres    false    219            �           2606    24872 )   passeggero passeggero_numerodocumento_key 
   CONSTRAINT     p   ALTER TABLE ONLY public.passeggero
    ADD CONSTRAINT passeggero_numerodocumento_key UNIQUE (numero_documento);
 S   ALTER TABLE ONLY public.passeggero DROP CONSTRAINT passeggero_numerodocumento_key;
       public                 postgres    false    220            �           2606    24874    passeggero passeggero_pkey 
   CONSTRAINT     c   ALTER TABLE ONLY public.passeggero
    ADD CONSTRAINT passeggero_pkey PRIMARY KEY (id_passeggero);
 D   ALTER TABLE ONLY public.passeggero DROP CONSTRAINT passeggero_pkey;
       public                 postgres    false    220            �           2606    24876    utente pk_utente 
   CONSTRAINT     T   ALTER TABLE ONLY public.utente
    ADD CONSTRAINT pk_utente PRIMARY KEY (username);
 :   ALTER TABLE ONLY public.utente DROP CONSTRAINT pk_utente;
       public                 postgres    false    225            �           2606    24878    posto posto_pkey 
   CONSTRAINT     ^   ALTER TABLE ONLY public.posto
    ADD CONSTRAINT posto_pkey PRIMARY KEY (codice_volo, posto);
 :   ALTER TABLE ONLY public.posto DROP CONSTRAINT posto_pkey;
       public                 postgres    false    222    222            �           2606    24880 -   prenotazione prenotazione_numerobiglietto_key 
   CONSTRAINT     t   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT prenotazione_numerobiglietto_key UNIQUE (numero_biglietto);
 W   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT prenotazione_numerobiglietto_key;
       public                 postgres    false    223            �           2606    24882    prenotazione prenotazione_pkey 
   CONSTRAINT     i   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT prenotazione_pkey PRIMARY KEY (id_prenotazione);
 H   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT prenotazione_pkey;
       public                 postgres    false    223            �           2606    24884    volo volo_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.volo
    ADD CONSTRAINT volo_pkey PRIMARY KEY (codice);
 8   ALTER TABLE ONLY public.volo DROP CONSTRAINT volo_pkey;
       public                 postgres    false    226            �           1259    24885    fki_o    INDEX     B   CREATE INDEX fki_o ON public.prenotazione USING btree (username);
    DROP INDEX public.fki_o;
       public                 postgres    false    223            �           2606    24886 %   bagaglio bagaglio_idprenotazione_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.bagaglio
    ADD CONSTRAINT bagaglio_idprenotazione_fkey FOREIGN KEY (id_prenotazione) REFERENCES public.prenotazione(id_prenotazione) ON DELETE CASCADE;
 O   ALTER TABLE ONLY public.bagaglio DROP CONSTRAINT bagaglio_idprenotazione_fkey;
       public               postgres    false    217    223    4793            �           2606    24891 "   prenotazione fk_prenotazione_posto    FK CONSTRAINT     �   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT fk_prenotazione_posto FOREIGN KEY (codice_volo, posto) REFERENCES public.posto(codice_volo, posto) ON UPDATE CASCADE ON DELETE RESTRICT;
 L   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT fk_prenotazione_posto;
       public               postgres    false    222    222    223    223    4788            �           2606    24896    volo gatefk    FK CONSTRAINT     j   ALTER TABLE ONLY public.volo
    ADD CONSTRAINT gatefk FOREIGN KEY (gate) REFERENCES public.gate(numero);
 5   ALTER TABLE ONLY public.volo DROP CONSTRAINT gatefk;
       public               postgres    false    226    219    4782            �           2606    24901    posto posto_codice_volo_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.posto
    ADD CONSTRAINT posto_codice_volo_fkey FOREIGN KEY (codice_volo) REFERENCES public.volo(codice) ON DELETE CASCADE;
 F   ALTER TABLE ONLY public.posto DROP CONSTRAINT posto_codice_volo_fkey;
       public               postgres    false    226    222    4797            �           2606    24906 )   prenotazione prenotazione_codicevolo_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT prenotazione_codicevolo_fkey FOREIGN KEY (codice_volo) REFERENCES public.volo(codice) ON DELETE CASCADE;
 S   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT prenotazione_codicevolo_fkey;
       public               postgres    false    4797    223    226            �           2606    24911 +   prenotazione prenotazione_idpasseggero_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT prenotazione_idpasseggero_fkey FOREIGN KEY (id_passeggero) REFERENCES public.passeggero(id_passeggero) ON DELETE CASCADE;
 U   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT prenotazione_idpasseggero_fkey;
       public               postgres    false    220    223    4786            �           2606    24916 '   prenotazione prenotazione_username_fkey    FK CONSTRAINT     �   ALTER TABLE ONLY public.prenotazione
    ADD CONSTRAINT prenotazione_username_fkey FOREIGN KEY (username) REFERENCES public.utente(username) NOT VALID;
 Q   ALTER TABLE ONLY public.prenotazione DROP CONSTRAINT prenotazione_username_fkey;
       public               postgres    false    223    225    4795            V   9  x���=o1�:�_@�o�$��MIsD'��ϬDP.�\{�v�����������X��N�������ק�����o��fȴD��h�!ˢ��VnUZ�V�S�I�a�Y ���/���`���"��W�Y�m?mb�/7�	�ܳ`�|�l�!�r>��a�����]�=K�ڳA&!Ρ�lIEɲ�[�2�Ve�ι9@3��q&��1��s�ҳB^>�&�6߼@m3d�YO�`箳@@R��*] �i�(W�!����#cB�"��7\n��pþL��7kW%��Z���W9�9��>ojyZ��7ܰ3�Y��+�.7\o�a�c�!AUK\�G'v9c7��*^TvG�=��/���O���G=�z�8Seʮ���D��g�Y{6�a��Y���J�
umE���ZJdV�/S���-C�~�8??_^4* �*p�V����ck���MN�����{ȴ��|9j���"�F�ܳ���u6���x/枥g�� ��q�3�9V����UK���b`ьg1��\jY +{MP���g�L^�&p�g������:X�ku�"�d�p؋uX���~8y�lH枥g��&��8��^�,1����4t�I枥g�� �#>G>�5�sshYz֞�Z�%L��;"ս�<OfȻJ2����ܳ��=d�j��5��Y/�*2�:��H��0&U�4����љP�>*Չ��j_�M��3��}?�����}��7B���F��ͣ5B��B�<#��i�G��ѡTΝM_5Euօ��}��9Wm�b}0�Wo���x�)T�v�F������e_ޟN��c~b      X      x�3�2�2�2����� �      Y   -  x�UV�n�6]�_�]w�D���mI��ؐ|oP���u�H��G�f�?�?�Kz���tc���9s�ሀ=�aВ�����`��N�.
.Blu����X�b�E���zU�ɜ�I�,���@HL!���,N�<�"��T�{�:-{[�r<k�G�^�~Ъ3��8ۂ{.[��i���m+?���y��^��`i'[[RY���+�ylQ�q�zX�݀;_��AY�?m�SL�|Q��{�B7�a{�KF�l�͆{!n�x+폖~����Q����ӟ��Yu���9E�,�^���6l��
'�J���V��{5t�Y�2�c�󷲗���,���s�e�����jǾ7,��(F��Q�'@l�vJ��n�%P�v���~�6�W�7���9�����0**�l�cg+�	�d"�������q�fa��~|��a�ؐ���/���mH����<pX����-�8����~�ג�zΞM;�Z�DK�ew����v/Uzՙ�}ǩ���;��"��b�߃�y��@U�]Օ�ϐz  Q�҄�Y��o�q�x��[�����
��Ķ�H\�B>�L7�<�(�<Hn�"3|��C��#"J�QcaN�AG�� ņ�E�k�&�>_�����eڴ�>����P+�$
�=���a������?�x��X �П4���5$Z�v,[��W��0�GQ��J���Nq{����j��Z[�EV�0��>�> �tb�zB���z
�k	�z�yq�\�0�@�q���'PK�ࣕ��]p���䑋�.��~[�
0��4��b�#qO����K�YI��X�e�"\�'+6�QV�O9 ��ZI��7� ˉ𼕍=�k�#-��e�J�gtx���E"�$#��Ԓ�,TY���@�ю�f����=�Rxi�#@�h�����+��ؙ���5��y�C#�x�a?.ē*�>�x,f#*T��5�co>���P�
Y���IZ�557v���`��B��WU��!{��kie>ZY�(q�V�ֵ�l%/r*��a�$�
ήZ��8��8 G��� �U��ˊq�O���5n��	_���幸$�J��Y���b7���C1��`���+6�*��[��NWRT���$��)$S�F�v��tUH0���m���%��L>I(2Dm�D�,�-g���wkR�KXiz�����ֳ�S�]ǹ�=�N^.��筱��o� ��#���-gݓ?ɩ�D8��H���� WO�P���Q�.#r��sS
غ�4��d=6�9�u������&��'\�ct�cw�ԃt�����L�GY�Mm&�>a�'�o��>����k�8��u���fZ����]�"��L��2��yb�:�u�㊉��MwyS�b�;��z��r/�Mi�=�E��)q��r�����Ph ����?`J������0<�@Љ@ע�F�<B�d\{"��8����EϞ�|�6Po�	�.�V�����g
ւ�{���c��nB�tأ:0��˜&�N
����l57��YEج� ��4��h��-c�-FC��-��ԇ��5�-�8�Q�}9{)@W�(���Z����<�N�_x��#6m3h�)@��k��z��P�C��O�zaU�G&����.�OӲ��;n�Л�Ec��~ލGRxEU�yt/���b�_g&�<*��u�;"�&��[�١�++���؍��b���5�r�lh9F��s��l~!!�?0o��/�y[�i��P�IWZ]�'��;Qf҃i��ʟ�g�|O��-b�M����iy���Ƽ��U��'C�>�b�i����'mR����� k��      [      x�U��n$I��u���ka�"����A� ����!Me&��΃~�]uO�����?�wu�?�������|��Y�y���'�/�䄜���49MN���49��E�"g���Y�������������9Cΐ3�9C�E�E�E�E�E�E�M�M�M�M�M�MN}���Ӌ//��e�0�L+�ʴ��[��vY��e��~�/^V��xY��e�˞�E/�^V��zY���e�˾��/_V��|Y���e��ޗ�/�_V��~Y���e���� J%��@��TP2(�J	%��B�-D�B�-D�B�-Dٞ�ۃ~{�o���Y�=�-D�B�-D�B�-D�B�-D�B�-�ia�������X�������?ak��󽚽��fo��ٻػ����E�b��ރ�{�|�NrNrNrNr�G��?������~�ڲ��{������y��{N>��ن�6|����C�E�E�E�E�E�E�M�M�M����N~X�[������´��[�˴��V���}\���}^���D)�$�z�>/LSHI�4R")��LJ'�w���4ٔnJ8���Nm��zJ>���;�ya�u/�^�l|Y���e��֗�/{_�l~Y���e�_���iZ���h!Z���h!Z���l�-d{h!۫a{7l/-D�B�-D�B�-D�B�-D�B�-D�;7Z���h!Z���h!Z���h!Z���h!Z���h!Z���h���Zh-������C���C����?�>~����bMN�	9MN���49MN���Y�,r9��E�A�A�A�A�A�A�I�I�I�I�I�Iΐ3�9����t���_������ׇ�;|ߋ�{�}/�����ȹȹɹɹɹɹɹ�yv��s4���9��a�h;Gs�9����v��s4���9��a�h;Gs�9����v��s4���9��a�h;Gs�9����v��s4���9��a�h;Gs�9����v��s4���9��a�h;Gs�9����v��s4���9��a�h;Gs�9����v��s4���9��a�h;����	��PR(-D�B�-D�B�-D�B�������7��j����A�B�-D�B�-D�B�-D�B�-D���s��*<�_�����/�c������c�^�W���49MN���,r{{{{��=�{��_���'��$������������wȹȹȹȹȹȹȹɹɹɹɹɹ���//lЇ���ŷ��i[�B�i���N|\Xֲ�e]˾��-[V��lY��q�y������~�)ѣ�~��e�����/�ޭ���P�(E�$^���i
)���<I��d�z�>/LSPI�4T"*��JG%�RRI��Tb*5��JO%�RTI�4MES�T4MES�T4MES������� �+`{l/MES�T4-D�B�-D�B��x����8Ϗ���k��^��x�?Y�fo�K~���49MN���,r9��E�"�ރ������{N�9�g�g�g��➋{.��3_|��|�s�s�s�s�s�{��ˋO/����^�V��ie��0+�x��.,\ٸ�re��ҕ�+kW��,^ټ�ze����+�W��,`m�`�������υi�i��lkY�����´�4k]��,����saژ&�R@I�4P"(�J%�RBI����¯����?l�׏������ᓯ{ϟ��p�=�9_��?�8#Έ3�8#Έ3����g����:���a{#�3�8#Έ3�8#Έ3�8#��uqF�g�qF�g�qF�g�qF�g�qF�g�qF�g�qF�g�qF�-��Bk���Zh-�Z���Bk���Zh-���}����X���߬�Y�y���&��ir��&g���Y�,r9��������������������������!g�r��!gȹȹȹȹȹȹȹɹɹɹɹɹ�y�՞�^|y�ۋo/L+�ʴ2�L+��4�]���w���e�ˊ�/K^���y���e�˪�]/�^���{����e��ʗ�/K_���}����e����/�_�����@)�$P(�
J��z���O�_���f����JQ%��T4MES�T4MES�T4MES�T�����^�;c{ih*�����h*�����h*�����h*�����h*�����h*�����h*�����h*�����h*�����h*�����h!Z�Z���Bk���Zh-�Z���Bk���Z؎X���Bo'������C�v�ڎQZh-�Z���Bk���Zh-�Z���Bk���Z���������ך����x�?Y�fo���nr��&��ir��E�"g���Y�,rr��=�{��d��g8�9����$g�r��!g�{��b��ދ���"�&�&�&�&�&�&�qh������{�a?L+�ʴ2mk��}Z_��lqY��e��&�U.�\��lsY��e��F�="�ֿ��0�������~9�J�������sa�BJ"��I��dR:�큤��Ji��Rjyx.LO���S�)��JB��Q��dT:�����(:���tt����X?<�_���f��f��59MN���,r9��E�������:��$�d���|�a�w�;|�!gȹȹȹ�{��b��ޛ�7��&�&������^|y�ۋo/L+�ʴ2�L+��aY����ya��,�Y6��f�Ͳ�e;�z��,Z6��h����ya��}=Q����ί'���r��.��z�>/L��e�˾��/_V��|Y����'���4�
J��B)��PZ���h!Z��'j�-D�B��홼=�����=������h!Z���h!Z���h!Z��Y��{Ty�?Y�f��^�|}���kr.��ף�h0���?�{���_�O�_���fMN���4{���������gX�,r999999�����=�g�g�^�����אs�s�s�s�s�s��f��ޛ��{��w��ŧ_^�����ʴ2�L+�ʴ2͂�-+Zv�,i�Ҳ�eO�֟Ӭm�۲�es���-�[���o�߲�e��
�.K�x��\�v�v�f�˺�}/_6��|����e��ڗ�/��x��.dP:(!�J
��h!Z���h!Z���h!Z���l��i�=�������B�-D�B�-D�B�-D�B�-D�B����-D�B�-D�B�-D�B�-D�B�-D�B�-D�Bk���Zh-�z{/����Z��������u��=��&�_��]�u׺kݵ�Zw���]�u��Ii;*mg%��vrڎN�kݵ�Zw���]�u׺kݵ�Zw���]�u׺맻~N�׿YsO���[�X�fo���nr��&��ir��E�"g��ػ�{��`��ރ�pr��='�������|��]�\�\�\�\�\��������������ϋO/���7��ô�jeZ�������KY���e�˲�e3�j��,�Y���g�ϲ�eCˊ����llYٲ�ei�־���,�����0�~���8M��e����/�_��P
(	�J���A頄PJ()���h!Z���h!Z���h!�#Y���z{Do���!��h!Z���h!Z���h!Z���h!Z���h!������<�d��^����{}����߿��8�������߬�Y�y�CN�	9!'䄜&��ir��&��Y�,r9��E�"�$�$�$�d���a�w�;|�!gȹȹ�{q���7��|ߛ�7��&�&��>������~|X�Ӷ�m]�ʶ�m��}+W6��\ٹ�te��ڕ�+�W6��^ٽ�|e�����+X6��>��0�0�0�0�0�B��.+�x��\�f�ˊ�/K^���y�����υi�i"(�J%�RBI��-D�B�-D�B�-D�B�g���ݞ���w{�n`-D�B�-D�B�-D�B�-D�B�-D�B�-D�B�-D�B�-D�B�-D�B�-D�B�-D�Bk���Zh-�Z���Bk���Z�>�Ǚ��g��7k�<�k��z�[_���W�����턳q�3�v��N9�1G_���W����j}��Z_���W����j}��Z_���W����j}��Z_���W����j} �	  ��Z_���W��������������������������������������������������3�v��N�۱;�o-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-,-Z8�ph���������ߵ����a6q�Mf���a6q�Mf���a6q�M���aq�Af��aq�Af��aq�Af��aq�Af��a�p�/����a�p�/����a�p�/����q�p�/����q�p�/����q�p�/����q�p�/���v=>^�bs?���u�/�_�?Y{?9����_�+��RZ)��ZJ.��L)�=�89�C���8�89�C���8�89�C���8�89�C���8�89�C���8�89�C���8�89�C���8�89�C��p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�p�8�8'�É�N��0M���Bk���Zh-�Z���Bk���Zh-�Z����������������������������������p�~��h��k�y�������fo�k���c��nr��&��ir�����������ރ�p�s�s�s��d��ޓ�'{O>�Iΐ3��{��{/>��ދ�7����}o������~Xy^ؠk���e�V���[	˴2�N��,[Y[��eY̲�e5�n��,�Y�������4�Zֵ�kYز�ee�Ζ�}�h�0��%.[\ָ�qY�ɯ��Y��e��n��~�h�0ͮ�~4x\����e����/�������y�cP�B�-D�B�-d{>o��	�=��g���h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h���Zh-�Z���Bk���Zh-�Z���Bo��Yg>�닯��֯�z��~���}���v��N?��GT-�U��EբjQ��ZT-�U��EբjQ��ZT-�U��EբjQ��ZT-�U��EբjQ��ZT-�U��E�D�D�D�D�D�D�D�D�D�D�D������zN�&���]�X�y������'�/�䄜���49MN���49��E�"g���Y�������������9Cΐ3�9C�E�E�E�E�E�E�M�M�M�M�M�M��oTx^|z��]�����ieZ�V�mն�e��v���͉/^V��xY��e�˞�E/�^V��zY���e�˾��/_V��|Y���e��ޗ�/�_V��~Y���e���� J%��@��TP2(�J	%��B�-D�B�-D�B�-d�����X�~�������6�^��`{!�%z�^���%z�^���%z�^���%z�^���%z�^���%z�^���%z�^���%z�^���%z�^���%z�^���%z�^Z/���K���zi��^Z/���Kk���Zh-�Z������v>�H�	i;"i���Zh-�Z���Bk���Zh-�Z���Bk���Zh-�Z���Bk���Zh-�Z���Bk���Zh-������������������7����������������#��3��C�������������������������������-Z8�ph����0u\��p�O�_����c�c���o�߬�����?�|��34����ghr��E�"g���Y�,rr��=�{��d��g8�9�9�9�r��!g�r������������7{o����{�nr^??/>���}|{aZ�V��ieZ�V�	�P�̀E.�\V��rY��e��>��.]V��tY��e�_??/L��e�˞�E/�^V��zY���e�˾��/_V��|Y���e��޿~
~^�&��A	��PR(-D�B�-Dٞ�Z���h!Z�������:�B�����h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h!Z���h���Zh-�Z���Bk���Zh-�Z���Bk����	g;�lg-�z;�lG]�.Z���E�uѺh]�.Z���E�uѺh]�.Z���E�uѺh]�.Z���E�uѺh]�.Z������������������������������������_����g����(�k��㜩���(�[��'���z�?\����6#���G��?=׏�?��2����A��?v1��G{�"���7l���������N�      \   c  x�u�K�^7��>?��&Y^&i
��BZ
�a
�t�����u>�be��̃-��OJ�~�4f�9=��TH8��ӟ����^�yN�������5��� ��Sy���� ՞ʇ�k9��[i��:��s ���C`W�eb�=��C@����r.�KNr8��_~jB�((c��>�:|��8�����$�����3l��/�|Q���T�^m8ؗ+��� �A2�z�z�_�aX[z�q�!@׻-�یM�չ�v/SҌl���lN�	|A\`�ԙ3Qt"9��n=�D�\zK�-�A����wHo:��8�E�L����xM��aY�%QA����5�ā<T���Ƅ�<�v!m䊡�����ӧ����l��(+ ��+�e������N��9�\~wҢ���h�#- N�.W��v��9;��X;�U=��8i�,C�38����
��$>��eC+6c��R����"[;��Ҏ@rЂ�J�A �Akh��rF�ppŦ���ΈsV_R$�Q^ ��+4Mp�����٪AfP� �����H�, vtq4�F6�������P�*��F!���6>i�/�.�IpG$���	
m���rz��(�9k81�@�Z� H���9&�Q4��l� �Z����ԙ��Mq}��:�8���rǪ"d�F�w�� [�ѼX\2��� l.gE݆6v;
�ߣ��D�o���{��撳�K�._]��C�n2�>-s�O���%W,��%7'm�����`�s�$��lgJ҃T��%�QV*:+�ڼ�Rth��� �e��A�{�~F���-y������*Z6J��FVEi{�l%]	�Cy��LE����[��TE���+*�7d���\���IX�Ά��.Y[�m#D��K��GcWɺI�,�����6jm��76�o��.Ȁ��Q���M<k�N�;�2�������ƙ>�U�U+��gQ�s�V+:�ڼ��jz=c������#4<�n��3�����Q�4�XUb����.ٺ~A�l�篯������V��fy���V�Ő'��MШV�����ۊ(|��������˗緯�~����J�����ZM~�e@F�p k��[��Xm��ӿ�m����(WG��&��Z��}���+c$-�p�Z���6�K�lE�-9�m����sf�ְhz�F�^)�*�7j��n�-L���m�H�������Bi��K��� ����q4a���Q�[�ОU�h٨}T��4TtW��EL���Aɶɻ�b�˱(�O0��-���%����@_��0Vtn�^�1�ϱ%�^��~h:_�F ^�t�w�룚��(�������Җ��Ѵ���.���ɏ�L���'�>y��|A����w�u�$L�      ^   �   x�U��N�0���/O�'@�ƿ+�ː���X�U,:%�e��{�ȉC�Qt�9N���2ㅲV��?�A��G)�G!m�*�"rI��
>8w�Fgu^#ڳr���*jx.?Vĭ����8�O�ڍ٥T�E����m�祪�F�|�|��܅�At�;ʃ����Yv��х6�29	��r����U)ϣ��Y�T�=�}��i��y�R����+      _   �  x�m�_��0ş�O�/�K��}�,�������y��l7`MISv;�~ou���{��.�Z��
-XO���K���N��FS|U9�9��i<� ��?��w�#�**�d��f���/\�wZ�mѲ�ӎ&�=����y�7܀��Z�1A�2�$^�9k��$d��ʴ���f��iCؘ'��"\�cB�id��t����2�t��vo��7��f&��~a�k���l�����	�~ܨ�*���O���0��<�X����7�mK�����^�ۃ��2�|��cR�S\��Զ�Z��0��["��\�Rt���R�ɜÂ*��WŲ[kI&���RѨ��;�*��a	5Fm_����E�Z��AY�65YT)�zy��0� 1�Y]!j�w}�J�qy/�m�uK��`�Ӕ��x�o8�:k��R���^�oaZ�����?��.5     