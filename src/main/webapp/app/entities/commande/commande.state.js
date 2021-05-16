(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('commande', {
            parent: 'entity',
            url: '/commande',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.commande.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commande/commandes.html',
                    controller: 'CommandeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commande');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('commande-detail', {
            parent: 'commande',
            url: '/commande/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.commande.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/commande/commande-detail.html',
                    controller: 'CommandeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('commande');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Commande', function($stateParams, Commande) {
                    return Commande.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'commande',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('commande-detail.edit', {
            parent: 'commande-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-dialog.html',
                    controller: 'CommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commande', function(Commande) {
                            return Commande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commande.new', {
            parent: 'commande',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-dialog.html',
                    controller: 'CommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date_commande: null,
                                designation: null,
                                piece_jointe: null,
                                piece_jointeContentType: null,
                                reference: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('commande', null, { reload: 'commande' });
                }, function() {
                    $state.go('commande');
                });
            }]
        })
        .state('commande.edit', {
            parent: 'commande',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-dialog.html',
                    controller: 'CommandeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Commande', function(Commande) {
                            return Commande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commande', null, { reload: 'commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('commande.delete', {
            parent: 'commande',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/commande/commande-delete-dialog.html',
                    controller: 'CommandeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Commande', function(Commande) {
                            return Commande.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('commande', null, { reload: 'commande' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
