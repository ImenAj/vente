(function() {
    'use strict';

    angular
        .module('pfeApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('condition-de-reglement', {
            parent: 'entity',
            url: '/condition-de-reglement',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.condition_de_reglement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/condition-de-reglement/condition-de-reglements.html',
                    controller: 'Condition_de_reglementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('condition_de_reglement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('condition-de-reglement-detail', {
            parent: 'condition-de-reglement',
            url: '/condition-de-reglement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'pfeApp.condition_de_reglement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/condition-de-reglement/condition-de-reglement-detail.html',
                    controller: 'Condition_de_reglementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('condition_de_reglement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Condition_de_reglement', function($stateParams, Condition_de_reglement) {
                    return Condition_de_reglement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'condition-de-reglement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('condition-de-reglement-detail.edit', {
            parent: 'condition-de-reglement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/condition-de-reglement/condition-de-reglement-dialog.html',
                    controller: 'Condition_de_reglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Condition_de_reglement', function(Condition_de_reglement) {
                            return Condition_de_reglement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('condition-de-reglement.new', {
            parent: 'condition-de-reglement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/condition-de-reglement/condition-de-reglement-dialog.html',
                    controller: 'Condition_de_reglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                condition: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('condition-de-reglement', null, { reload: 'condition-de-reglement' });
                }, function() {
                    $state.go('condition-de-reglement');
                });
            }]
        })
        .state('condition-de-reglement.edit', {
            parent: 'condition-de-reglement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/condition-de-reglement/condition-de-reglement-dialog.html',
                    controller: 'Condition_de_reglementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Condition_de_reglement', function(Condition_de_reglement) {
                            return Condition_de_reglement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('condition-de-reglement', null, { reload: 'condition-de-reglement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('condition-de-reglement.delete', {
            parent: 'condition-de-reglement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/condition-de-reglement/condition-de-reglement-delete-dialog.html',
                    controller: 'Condition_de_reglementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Condition_de_reglement', function(Condition_de_reglement) {
                            return Condition_de_reglement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('condition-de-reglement', null, { reload: 'condition-de-reglement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
