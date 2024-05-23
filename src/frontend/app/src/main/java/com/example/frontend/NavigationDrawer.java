package com.example.frontend;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Método para obter o tipo de usuário.
 *
 * Este método faz uma solicitação GET para obter o tipo de usuário do servidor.
 * Dependendo do tipo de usuário obtido na resposta, executa a ação correspondente,
 * como excluir a conta ou redirecionar para a activity correta.
 */
public class NavigationDrawer extends Fragment {

    GerenciadorToken token;
    private RequestQueue requestQueueInfo;

    String userType;

    public void getUserType() {
        String urlInfo = Constants.BASE_URL + "/user-type";

        JsonObjectRequest jsonObjectRequestInfo = new JsonObjectRequest(Request.Method.GET, urlInfo, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Aqui você recebe o tipo de usuário na resposta
                        userType = response.optString("userType");
                        Log.d("User type dentro na navigation", userType);
                        deletarContaReq(userType);
                        // Redirecionar para a activity correta com base no tipo de usuário
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Tratar erro
                String errorMessage = "error";

                if (error instanceof NetworkError) {
                    errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
                } else if (error instanceof ServerError) {
                    errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
                } else if (error instanceof ParseError) {
                    errorMessage = "Houve um problema ao processar a resposta do servidor.";
                } else if (error instanceof TimeoutError) {
                    errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
                }

                // Exibir mensagem de erro
                new AlertDialog.Builder(getActivity())
                        .setTitle("Erro")
                        .setMessage(errorMessage)
                        .setPositiveButton("OK", null)
                        .show();
                Log.e("VolleyError", errorMessage);
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token.getToken()); // Substitua "Bearer " + token pelo seu token JWT
                return headers;
            }
        };

        requestQueueInfo = Volley.newRequestQueue(requireContext());
        requestQueueInfo.add(jsonObjectRequestInfo);
    }

    /**
     * Método para solicitar a exclusão da conta do usuário.
     *
     * Este método faz uma solicitação DELETE para excluir a conta do usuário ou parceiro,
     * dependendo do tipo de usuário. O método trata as respostas e os erros da solicitação
     * e executa ações apropriadas, como limpar o token JWT, redirecionar para a tela de login
     * ou para a tela inicial após a exclusão da conta.
     *
     * @param userType O tipo de usuário, pode ser "partner" para parceiro ou qualquer outra coisa para usuário.
     */
    public void deletarContaReq(String userType) {
        String deleteUrl;

        if(userType.equals("partner")) {
            deleteUrl = Constants.BASE_URL + "/parceiro/deletar-parceiro";
        } else {
            deleteUrl = Constants.BASE_URL + "/usuario/deletar-usuario";
        }

        RequestQueue queue = Volley.newRequestQueue(getActivity());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, deleteUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Trate a resposta JSON aqui
                        System.out.println(response.toString());
                        token.clearToken();
                        Toast.makeText(getActivity(), "Conta deletada com sucesso.", Toast.LENGTH_SHORT).show();

                        // Redireciona para a tela inicial após 3 segundos
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent retornarTelaInicio = new Intent(getActivity(), TelaInicio.class);
                                startActivity(retornarTelaInicio);
                            }
                        }, 3000); // 3000 milissegundos = 3 segundos
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Trate o erro aqui
                String errorMessage = "error";

                if (error.networkResponse!= null && error.networkResponse.statusCode == 401) {
                    token.clearToken();
                    Intent intent = new Intent(getActivity(), FormLogin.class);
                    startActivity(intent);
                } else if (error instanceof NetworkError) {
                    errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
                } else if (error instanceof ServerError) {
                    errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
                } else if (error instanceof ParseError) {
                    errorMessage = "Houve um problema ao processar a resposta do servidor.";
                } else if (error instanceof TimeoutError) {
                    errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
                }


                new AlertDialog.Builder(getActivity())
                        .setTitle("Erro")
                        .setMessage(errorMessage)
                        .setPositiveButton("OK", null)
                        .show();
                System.out.println(error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Bearer " + token.getToken()); // Substitua "Bearer " + token pelo seu token JWT
                return headers;
            }
        };

        queue.add(jsonObjectRequest);
    }

    /**
     * Método chamado para criar a exibição do fragmento.
     *
     * Este método infla o layout do fragmento e configura os elementos de interface do usuário,
     * como botões para acessar o perfil, deletar conta e sair da conta. Ele também define os
     * ouvintes de clique para executar as ações apropriadas quando os botões são clicados.
     *
     * @param inflater           O LayoutInflater usado para inflar o layout do fragmento.
     * @param container          O ViewGroup pai ao qual a view gerada pelo inflater deve ser anexada.
     * @param savedInstanceState Um objeto Bundle contendo o estado anterior do fragmento,
     *                           que é usado para reconstruir o fragmento após uma mudança
     *                           de configuração, como uma rotação de tela.
     * @return A View do fragmento que foi inflada e configurada.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout do fragment
        View view = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        // Inicialize o objeto token com o contexto da atividade pai
        token = new GerenciadorToken(requireActivity());

        TextView nomeUsuarioDrawer = view.findViewById(R.id.nomeUsuarioDrawer);
        nomeUsuarioDrawer.setText(Constants.userName);


        Button botaoIrParaOPerfil = view.findViewById(R.id.buttonIrPerfil);

        if (!(getActivity() instanceof MainActivity)) {
            botaoIrParaOPerfil.setText("Home");
        }

        botaoIrParaOPerfil.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (getActivity() instanceof MainActivity) {

                    String urlInfo = Constants.BASE_URL + "/user-type";
                    JsonObjectRequest jsonObjectRequestInfo = new JsonObjectRequest(Request.Method.GET, urlInfo, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    // Aqui você recebe o tipo de usuário na resposta
                                    String userType = response.optString("userType");

                                    if (userType.equals("partner")) {
                                        Intent mudarTelaPerfilParceiro = new Intent(getActivity(),PerfilPaceiro.class);
                                        startActivity(mudarTelaPerfilParceiro);
                                    } else {
                                        Intent mudarTelaPerfilUsuario = new Intent(getActivity(),PerfilUsuario.class);
                                        startActivity(mudarTelaPerfilUsuario);
                                    }
                                    // Redirecionar para a activity correta com base no tipo de usuário
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String errorMessage = "error";

                            if (error.networkResponse!= null && error.networkResponse.statusCode == 401) {
                                Intent intent = new Intent(getActivity(), FormLogin.class);
                                startActivity(intent);
                            } else {
                                if (error instanceof NetworkError) {
                                    errorMessage = "Sem conexão com a internet. Por favor, verifique sua conexão.";
                                } else if (error instanceof ServerError) {
                                    errorMessage = "O servidor está enfrentando problemas. Por favor, tente novamente mais tarde.";
                                } else if (error instanceof ParseError) {
                                    errorMessage = "Houve um problema ao processar a resposta do servidor.";
                                } else if (error instanceof TimeoutError) {
                                    errorMessage = "A solicitação demorou muito para ser processada. Por favor, tente novamente mais tarde.";
                                }

                                new AlertDialog.Builder(getActivity())
                                        .setTitle("Erro")
                                        .setMessage(errorMessage)
                                        .setPositiveButton("OK", null)
                                        .show();
                            }

                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " + token.getToken());
                            return headers;
                        }
                    };

                    requestQueueInfo = Volley.newRequestQueue(requireContext());
                    requestQueueInfo.add(jsonObjectRequestInfo);

                } else {
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                }

            }
        });

        //Define o botão de deletar conta
        Button botaoDeletarConta = view.findViewById(R.id.botao_deletar_conta);

        botaoDeletarConta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Deletar Conta")
                        .setMessage("Você tem certeza que deseja deletar a conta?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d("CAIU NO IF SIM", "DELETAR CONTA E MUDA DE ACTIVITY");
                                getUserType();

                                // Aqui você pode adicionar a lógica para deletar a conta
                                // Por exemplo, chamar um método que faz uma requisição ao servidor
                            }
                        })
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Log.d("CAIU NO IF NÃO", "FECHAR DIALOG SEM FAZER NADA");
                                // Fechar o dialog sem fazer nada
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });


        // Testar depois se está saindo nas outras activities
        // Define o botão de sair da conta
        Button botaoSairConta = view.findViewById(R.id.botao_sair);

        botaoSairConta.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                token.clearToken();

                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).abrirDrawer();
                    Intent intent = new Intent(getActivity(), TelaInicio.class);
                    startActivity(intent);

                } else {
                    // Redirecionar para outra atividade após logout
                    Intent intent = new Intent(getActivity(), TelaInicio.class);
                    startActivity(intent);
                }
            }
        });

        return view;

    }
}