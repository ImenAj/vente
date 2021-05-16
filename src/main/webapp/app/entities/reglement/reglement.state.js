(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('reglement', {
            parent: 'entity',
            url: '/reglement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.reglement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reglement/reglements.html',
                    controller: 'ReglementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reglement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('reglement-detail', {
            parent: 'reglement',
            url: '/reglement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.reglement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/reglement/reglement-detail.html',
                    controller: 'ReglementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('reglement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Reglement', function($stateParams, Reglement) {
                    return Reglement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'reglement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('reglement-detail.edit', {
            parent: 'reglement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-dialog.html',
                    controller: 'ReglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reglement', function(Reglement) {
                            return Reglement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reglement.new', {
            parent: 'reglement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-dialog.html',
                    controller: 'ReglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date_Reglement: null,
                                libelle: null,
                                montant: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('reglement', null, { reload: 'reglement' });
                }, function() {
                    $state.go('reglement');
                });
            }]
        })
        .state('reglement.edit', {
            parent: 'reglement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-dialog.html',
                    controller: 'ReglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Reglement', function(Reglement) {
                            return Reglement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reglement', null, { reload: 'reglement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('reglement.delete', {
            parent: 'reglement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/reglement/reglement-delete-dialog.html',
                    controller: 'ReglementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Reglement', function(Reglement) {
                            return Reglement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('reglement', null, { reload: 'reglement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
